package com.delcam.domain.init;

import com.delcam.domain.code.CommonCode;
import com.delcam.domain.code.CommonCodeService;
import com.delcam.domain.program.Program;
import com.delcam.domain.program.ProgramService;
import com.delcam.domain.program.menu.Menu;
import com.delcam.domain.program.menu.MenuService;
import com.delcam.domain.user.User;
import com.delcam.domain.user.UserService;
import com.delcam.domain.user.auth.UserAuth;
import com.delcam.domain.user.auth.UserAuthService;
import com.delcam.domain.user.auth.menu.AuthGroupMenu;
import com.delcam.domain.user.auth.menu.AuthGroupMenuService;
import com.delcam.domain.user.role.UserRole;
import com.delcam.domain.user.role.UserRoleService;
import com.chequer.axboot.core.code.AXBootTypes;
import com.chequer.axboot.core.code.Types;
import com.chequer.axboot.core.db.schema.SchemaGenerator;
import com.chequer.axboot.core.model.extract.service.jdbc.JdbcMetadataService;
import com.chequer.axboot.core.utils.ArrayUtils;
import com.chequer.axboot.core.utils.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DatabaseInitService {

    @Inject
    private SchemaGenerator schemaGenerator;

    @Inject
    private UserService userService;

    @Inject
    private UserRoleService userRoleService;

    @Inject
    private UserAuthService userAuthService;

    @Inject
    private MenuService menuService;

    @Inject
    private CommonCodeService commonCodeService;

    @Inject
    private AuthGroupMenuService authGroupMenuService;

    @Inject
    private ProgramService programService;

    @Inject
    private JdbcMetadataService jdbcMetadataService;

    @Inject
    private JdbcTemplate jdbcTemplate;

    public boolean initialized() {
        return ArrayUtils.isNotEmpty(jdbcMetadataService.getTables());
    }

    @Transactional
    public void createBaseCode() throws Exception {
        List<String> lines = new ArrayList<>();

        List<Program> programs = programService.findAll(new Sort(Sort.Direction.ASC, "remark"));

        for (Program program : programs) {
            String line = String.format("programService.save(Program.of(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"));",
                    program.getProgCd(),
                    program.getProgNm(),
                    program.getProgPh(),
                    program.getTarget(),
                    program.getAuthCheck(),
                    program.getSchAh(),
                    program.getSavAh(),
                    program.getExlAh(),
                    program.getDelAh(),
                    program.getFn1Ah(),
                    program.getFn2Ah(),
                    program.getFn3Ah(),
                    program.getFn4Ah(),
                    program.getFn5Ah(),
                    program.getRemark()
                    );

            lines.add(line);
        }

        lines.add("\n");

        for (Menu menu : menuService.findAll()) {
            if (menu.getParentId() == null) {
                String line = String.format("menuService.save(Menu.of(%dL,\"%s\",\"%s\",JsonUtils.fromJson(%s), null, %d, %d, null));",
                        menu.getId(),
                        menu.getMenuGrpCd(),
                        menu.getMenuNm(),
                        JsonUtils.toJson(JsonUtils.toJson(menu.getMultiLanguageJson())),
                        menu.getLevel(),
                        menu.getSort());

                lines.add(line);
            } else {
                String line = String.format("menuService.save(Menu.of(%dL,\"%s\",\"%s\",JsonUtils.fromJson(%s),%dL, %d, %d, \"%s\"));",
                        menu.getId(),
                        menu.getMenuGrpCd(),
                        menu.getMenuNm(),
                        JsonUtils.toJson(JsonUtils.toJson(menu.getMultiLanguageJson())),
                        menu.getParentId(),
                        menu.getLevel(),
                        menu.getSort(),
                        menu.getProgCd());

                lines.add(line);
            }
        }

        lines.add("\n");

        for (CommonCode commonCode : commonCodeService.findAll()) {
            String line = String.format("commonCodeService.save(CommonCode.of(\"%s\",\"%s\",\"%s\",\"%s\",%d));",
                    commonCode.getGroupCd(),
                    commonCode.getGroupNm(),
                    commonCode.getCode(),
                    commonCode.getName(),
                    commonCode.getSort());

            lines.add(line);
        }

        lines.add("\n");

        for (AuthGroupMenu authGroupMenu : authGroupMenuService.findAll()) {
            String line = String.format("authGroupMenuService.save(AuthGroupMenu.of(\"%s\",%dL,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"));",
                    authGroupMenu.getGrpAuthCd(),
                    authGroupMenu.getMenuId(),
                    authGroupMenu.getSchAh(),
                    authGroupMenu.getSavAh(),
                    authGroupMenu.getExlAh(),
                    authGroupMenu.getDelAh(),
                    authGroupMenu.getFn1Ah(),
                    authGroupMenu.getFn2Ah(),
                    authGroupMenu.getFn3Ah(),
                    authGroupMenu.getFn4Ah(),
                    authGroupMenu.getFn5Ah());
            lines.add(line);
        }

        String code = System.getProperty("user.home") + "/Desktop/code.txt";

        IOUtils.writeLines(lines, null, new FileOutputStream(new File(code)), "UTF-8");
    }


    public void init() throws Exception {
        createSchema();
    }

    public void createSchema() throws Exception {
        dropSchema();
        schemaGenerator.createSchema();
        createDefaultData();
    }

    public void createDefaultData() throws IOException {
        User user = new User();
        user.setUserCd("system");
        user.setUserNm("시스템 관리자");
        user.setUserPs("$2a$11$ruVkoieCPghNOA6mtKzWReZ5Ee66hbeqwvlBT1z.W4VMYckBld6uC");
        user.setUserStatus(Types.UserStatus.NORMAL);
        user.setLocale("ko_KR");
        user.setMenuGrpCd("SYSTEM_MANAGER");
        user.setUseYn(AXBootTypes.Used.YES);
        user.setDelYn(AXBootTypes.Deleted.NO);
        userService.save(user);

        UserRole aspAccess = new UserRole();
        aspAccess.setUserCd("system");
        aspAccess.setRoleCd("ASP_ACCESS");

        UserRole systemManager = new UserRole();
        systemManager.setUserCd("system");
        systemManager.setRoleCd("SYSTEM_MANAGER");
        userRoleService.save(Arrays.asList(aspAccess, systemManager));

        UserAuth userAuth = new UserAuth();
        userAuth.setUserCd("system");
        userAuth.setGrpAuthCd("S0001");
        userAuthService.save(userAuth);
        programService.save(Program.of("api","API","/swagger/","_self","N","Y","Y","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("main","메인","/jsp/main.jsp","_self","N","N","N","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("basic","기본 템플릿","/jsp/_samples/basic.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("login","로그인","/jsp/login.jsp","_self","N","N","N","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("axboot-js","[API]axboot.js","/jsp/_apis/axboot-js.jsp","_self","N","N","N","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("grid-form","그리드&폼 템플릿","/jsp/_samples/grid-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("grid-modal","그리드&모달 템플릿","/jsp/_samples/grid-modal.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("tab-layout","탭 레이아웃","/jsp/_samples/tab-layout.jsp","_self","N","N","N","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("ax5ui-sample","UI 템플릿","/jsp/_samples/ax5ui-sample.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("grid-tabform","그리드&폼탭 템플릿","/jsp/_samples/grid-tabform.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("page-structure","페이지 구조","/jsp/_samples/page-structure.jsp","_self","N","N","N","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("vertical-layout","좌우 레이아웃","/jsp/_samples/vertical-layout.jsp","_self","N","N","N","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("system-auth-user","사용자 관리","/jsp/system/system-auth-user.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("horizontal-layout","상하 레이아웃","/jsp/_samples/horizontal-layout.jsp","_self","N","N","N","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("system-config-menu","메뉴 관리","/jsp/system/system-config-menu.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("system-operation-log","에러로그 관리","/jsp/system/system-operation-log.jsp","_self","Y","Y","N","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("system-config-program","프로그램 관리","/jsp/system/system-config-program.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","Default"));
        programService.save(Program.of("system-config-common-code","공통코드 관리","/jsp/system/system-config-common-code.jsp","_self","Y","Y","Y","Y","N","N","N","N","N","N","Default"));
        programService.save(Program.of("item-form","제품관리","/jsp/mes/item-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("user-form","사용자관리","/jsp/mes/user-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("equip-form","장비/설비관리","/jsp/mes/equip-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("company-form","회사관리","/jsp/mes/company-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("process-form","공정관리","/jsp/mes/process-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("customer-form","거래처관리","/jsp/mes/customer-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("out-ipgo-form","외주입고","/jsp/mes/out-ipgo-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("shipping-form","출하관리","/jsp/mes/shipping-form.jsp","_self","Y","Y","N","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("item-repo-form","재고관리","/jsp/mes/item-repo-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("esmt-order-form","견적관리","/jsp/mes/esmt-order-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("exec-order-form","작업지시","/jsp/mes/exec-order-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("exec-result-form","실행관리","/jsp/mes/exec-result-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("out-request-form","외주요청","/jsp/mes/out-request-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("sales-order-form","수주관리","/jsp/mes/sales-order-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("exec-shipping-form","출하실행","/jsp/mes/exec-shipping-form.jsp","_self","Y","Y","N","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("process-group-form","공정그룹관리","/jsp/mes/process-group-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));
        programService.save(Program.of("quality-check-form","품질관리","/jsp/mes/quality-check-form.jsp","_self","Y","Y","Y","N","N","N","N","N","N","N","New"));

        menuService.save(Menu.of(1L,"SYSTEM_MANAGER","시스템 관리",JsonUtils.fromJson("{\"ko\":\"시스템 관리\",\"en\":\"System Management\"}"), null, 0, 0, null));
        menuService.save(Menu.of(2L,"SYSTEM_MANAGER","공통코드 관리",JsonUtils.fromJson("{\"ko\":\"공통코드 관리\",\"en\":\"CommonCode Mgmt\"}"),1L, 1, 0, "system-config-common-code"));
        menuService.save(Menu.of(3L,"SYSTEM_MANAGER","프로그램 관리",JsonUtils.fromJson("{\"ko\":\"프로그램 관리\",\"en\":\"Program Mgmt\"}"),1L, 1, 1, "system-config-program"));
        menuService.save(Menu.of(4L,"SYSTEM_MANAGER","메뉴 관리",JsonUtils.fromJson("{\"ko\":\"메뉴 관리\",\"en\":\"Menu Mgmt\"}"),1L, 1, 2, "system-config-menu"));
        menuService.save(Menu.of(5L,"SYSTEM_MANAGER","사용자 관리",JsonUtils.fromJson("{\"ko\":\"사용자 관리\",\"en\":\"User Mgmt\"}"),1L, 1, 3, "system-auth-user"));
        menuService.save(Menu.of(6L,"SYSTEM_MANAGER","에러로그 관리",JsonUtils.fromJson("{\"ko\":\"에러로그 관리\",\"en\":\"ErrorLog Mgmt\"}"),1L, 1, 4, "system-operation-log"));
        menuService.save(Menu.of(7L,"SYSTEM_MANAGER","샘플",JsonUtils.fromJson("{\"ko\":\"샘플\",\"en\":\"Samples\"}"), null, 0, 1, null));
        menuService.save(Menu.of(8L,"SYSTEM_MANAGER","기본 템플릿",JsonUtils.fromJson("{\"ko\":\"기본 템플릿\",\"en\":\"Basic Template\"}"),7L, 1, 4, "basic"));
        menuService.save(Menu.of(9L,"SYSTEM_MANAGER","페이지 구조",JsonUtils.fromJson("{\"ko\":\"페이지 구조\",\"en\":\"Page Structure\"}"),7L, 1, 0, "page-structure"));
        menuService.save(Menu.of(10L,"SYSTEM_MANAGER","좌우 좌우 레이아웃",JsonUtils.fromJson("{\"ko\":\"좌우 레이아웃\",\"en\":\"Left-Right Layout\"}"),7L, 1, 1, "vertical-layout"));
        menuService.save(Menu.of(11L,"SYSTEM_MANAGER","상하 레이아웃",JsonUtils.fromJson("{\"ko\":\"상하 레이아웃\",\"en\":\"Top-Bottom Layout\"}"),7L, 1, 2, "horizontal-layout"));
        menuService.save(Menu.of(12L,"SYSTEM_MANAGER","탭 레이아웃",JsonUtils.fromJson("{\"ko\":\"탭 레이아웃\",\"en\":\"Tab Layout\"}"),7L, 1, 3, "tab-layout"));
        menuService.save(Menu.of(13L,"SYSTEM_MANAGER","그리드&폼 템플릿",JsonUtils.fromJson("{\"ko\":\"그리드&폼 템플릿\",\"en\":\"Grid&Form Template\"}"),7L, 1, 5, "grid-form"));
        menuService.save(Menu.of(14L,"SYSTEM_MANAGER","그리드&탭폼 템플릿",JsonUtils.fromJson("{\"ko\":\"그리드&탭폼 템플릿\",\"en\":\"Grid&Form with Tab\"}"),7L, 1, 6, "grid-tabform"));
        menuService.save(Menu.of(15L,"SYSTEM_MANAGER","그리드&모달 템플릿",JsonUtils.fromJson("{\"ko\":\"그리드&모달 템플릿\",\"en\":\"Grid&Modal Template\"}"),7L, 1, 7, "grid-modal"));
        menuService.save(Menu.of(16L,"SYSTEM_MANAGER","UI 템플릿",JsonUtils.fromJson("{\"ko\":\"UI 템플릿\",\"en\":\"UI Template\"}"),7L, 1, 8, "ax5ui-sample"));
       
        commonCodeService.save(CommonCode.of("USER_STATUS","계정상태","ACCOUNT_LOCK","잠김",2));
        commonCodeService.save(CommonCode.of("USER_ROLE","사용자 롤","API","API 접근 롤",6));
        commonCodeService.save(CommonCode.of("USER_ROLE","사용자 롤","ASP_ACCESS","관리시스템 접근 롤",1));
        commonCodeService.save(CommonCode.of("USER_ROLE","사용자 롤","ASP_MANAGER","일반괸리자 롤",3));
        commonCodeService.save(CommonCode.of("LOCALE","로케일","en_US","미국",2));
        commonCodeService.save(CommonCode.of("LOCALE","로케일","ko_KR","대한민국",1));
        commonCodeService.save(CommonCode.of("DEL_YN","삭제여부","N","미삭제",1));
        commonCodeService.save(CommonCode.of("USE_YN","사용여부","N","사용안함",2));
        commonCodeService.save(CommonCode.of("USER_STATUS","계정상태","NORMAL","활성",1));
        commonCodeService.save(CommonCode.of("AUTH_GROUP","권한그룹","S0001","시스템관리자 그룹",1));
        commonCodeService.save(CommonCode.of("AUTH_GROUP","권한그룹","S0002","사용자 권한그룹",2));
        commonCodeService.save(CommonCode.of("MENU_GROUP","메뉴그룹","SYSTEM_MANAGER","시스템 관리자 그룹",1));
        commonCodeService.save(CommonCode.of("USER_ROLE","사용자 롤","SYSTEM_MANAGER","시스템 관리자 롤",2));
        commonCodeService.save(CommonCode.of("MENU_GROUP","메뉴그룹","USER","사용자 그룹",2));
        commonCodeService.save(CommonCode.of("DEL_YN","삭제여부","Y","삭제",2));
        commonCodeService.save(CommonCode.of("USE_YN","사용여부","Y","사용",1));

        authGroupMenuService.save(AuthGroupMenu.of("S0001",1L,"Y","Y","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",2L,"Y","Y","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",3L,"Y","Y","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",4L,"Y","Y","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",5L,"Y","Y","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",6L,"Y","N","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",7L,"Y","N","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",8L,"N","N","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",9L,"N","N","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",10L,"N","N","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",11L,"N","N","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",12L,"N","N","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",13L,"Y","Y","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",14L,"Y","Y","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",15L,"N","N","N","N","N","N","N","N","N"));
        authGroupMenuService.save(AuthGroupMenu.of("S0001",16L,"Y","Y","N","N","N","N","N","N","N"));

    }

    public void dropSchema() {
        try {
            List<String> tableList = schemaGenerator.getTableList();

            tableList.forEach(table -> {
                jdbcTemplate.update("DROP TABLE " + table);
            });
        } catch (Exception e) {
            // ignore
        }

    }
}
