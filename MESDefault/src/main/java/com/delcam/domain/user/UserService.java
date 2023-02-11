package com.delcam.domain.user;

import com.delcam.domain.BaseService;
import com.delcam.domain.user.auth.UserAuth;
import com.delcam.domain.user.auth.UserAuthService;
import com.delcam.domain.user.role.UserRole;
import com.delcam.domain.user.role.UserRoleService;
import com.chequer.axboot.core.parameter.RequestParams;
import com.querydsl.core.BooleanBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService extends BaseService<User, String> {

	private UserRepository userRepository;

	@Inject
	private UserAuthService userAuthService;

	@Inject
	private UserRoleService userRoleService;

	@Inject
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Inject
	public UserService(UserRepository userRepository) {
		super(userRepository);
		this.userRepository = userRepository;
	}

	@Transactional
	public void saveUser(List<User> users) throws Exception {
		if (isNotEmpty(users)) {
			for (User user : users) {
				delete(qUserRole).where(qUserRole.userCd.eq(user.getUserCd())).execute();
				delete(qUserAuth).where(qUserAuth.userCd.eq(user.getUserCd())).execute();

				String password = bCryptPasswordEncoder.encode(user.getUserPs());
				User originalUser = userRepository.findOne(user.getUserCd());

				if (originalUser != null) {
					if (isNotEmpty(user.getUserPs())) {
						user.setPasswordUpdateDate(Instant.now(Clock.systemUTC()));
						user.setUserPs(password);
					} else {
						user.setUserPs(originalUser.getUserPs());
					}
				} else {
					user.setPasswordUpdateDate(Instant.now(Clock.systemUTC()));
					user.setUserPs(password);
				}

				save(user);

				for (UserAuth userAuth : user.getAuthList()) {
					userAuth.setUserCd(user.getUserCd());
					if (userAuth.getGrpAuthCd().equals("S0001")) {
						UserRole aspRole = new UserRole();
						aspRole.setUserCd(user.getUserCd());
						aspRole.setRoleCd("ASP_ACCESS");
						UserRole systemRole = new UserRole();
						systemRole.setUserCd(user.getUserCd());
						systemRole.setRoleCd("SYSTEM_MANAGER");
						userRoleService.save(Arrays.asList(aspRole,systemRole));
					}else if(userAuth.getGrpAuthCd().equals("S0002")){
						UserRole apiRole = new UserRole();
						apiRole.setUserCd(user.getUserCd());
						apiRole.setRoleCd("API");
						UserRole aspRole = new UserRole();
						aspRole.setUserCd(user.getUserCd());
						aspRole.setRoleCd("ASP_ACCESS");
						userRoleService.save(Arrays.asList(apiRole,aspRole));
						
					}
				}

				userAuthService.save(user.getAuthList());
				//userRoleService.save(user.getRoleList());
			}
		}
	}

	public User getUser(RequestParams requestParams) {
		User user = get(requestParams).stream().findAny().orElse(null);

		if (user != null) {
			user.setAuthList(userAuthService.get(requestParams));
			user.setRoleList(userRoleService.get(requestParams));
		}

		return user;
	}

	public List<User> get(RequestParams requestParams) {
		String userCd = requestParams.getString("userCd");
		String filter = requestParams.getString("filter");

		BooleanBuilder builder = new BooleanBuilder();

		if (isNotEmpty(userCd)) {
			builder.and(qUser.userCd.eq(userCd));
		}

		List<User> list = select().from(qUser).where(builder).orderBy(qUser.userNm.asc()).fetch();

		if (isNotEmpty(filter)) {
			list = filter(list, filter);
		}

		return list;
	}
}
