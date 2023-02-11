package com.delcam.domain;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SequenceGenerator implements IdentifierGenerator , Configurable {
	   
	private String   SeqName;
	private String   prefix;
	private String   suffix ; 
	@Override
	public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
		SeqName = properties.getProperty("seqName");
	}

	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		    
		    Serializable result     = null;
	        Connection   connection = null;
	        Statement    statement  = null;
	        ResultSet    resultSet  = null;
	     
	        int          nextValue  = 1; 
	        String       remark     = ""; 
	      
	        try {
	            connection = session.connection();
	            statement = connection.createStatement();
	         
	            try {
	                resultSet = statement.executeQuery("SELECT MAX_ID FROM  COMMON_SEQUENCE WHERE SEQ_NAME ='"+ SeqName +"'" );
	            } catch (Exception e) {
	                System.out.println("In catch, cause : Table is not available.");
	            }
	            if (resultSet.next()) {
	            	nextValue = resultSet.getInt(1);
	            }
	            
	            switch (SeqName) {
	            	case "CO_ID":
	            		  prefix = "CO_"; remark = "회사정보" ; suffix = String.format("%08d", nextValue); break;
	            		  
		            case "CUST_ID":  
			              prefix = "C_";  remark = "거래처정보" ; suffix = String.format("%08d", nextValue); break;
			              
		            case "PROC_ID":  
		                  prefix = "P_";  remark = "공정정보"   ; suffix = String.format("%08d", nextValue); break;
		                  
		            case "PROC_GRP_ID":  
	               	      prefix = "G_";  remark = "공정그룹정보"   ; suffix = String.format("%08d", nextValue); break;
	               	      
		            case "EQUIP_ID":  
	               	      prefix = "E_";  remark = "장비정보"   ; suffix = String.format("%08d", nextValue); break;
	               	
		            case "ITEM_ID":  
	               	      prefix = "I_";  remark = "품목정보"   ; suffix = String.format("%08d", nextValue); break;
	               	
		            case "SO_ID":  
	               	      prefix = "SO_";  remark = "수주정보"   ; suffix = String.format("%08d", nextValue); break;
	               	
		            case "EXE_ID":  
	               	      prefix = "EO_";  remark = "실행정보"   ; suffix = String.format("%08d", nextValue); break;
	               	  
		            case "ESMT_ID":  
	               	      prefix = "EM_";  remark = "견적정보"   ; suffix = String.format("%08d", nextValue); break;
	              
		            case "EXEC_ID":  
	               	      prefix = "EX_";  remark = "작업지시"   ; suffix = String.format("%08d", nextValue); break;
	              
		            case "OUT_ID":  
	               	      prefix = "OP_";  remark = "외주공정"   ; suffix = String.format("%08d", nextValue); break;
	               	      
		            case "QC_ID":  
	               	      prefix = "QC_";  remark = "QC공정"   ; suffix = String.format("%08d", nextValue); break;
	               
		            case "SHIPPING_ID":
		            	  prefix = "SP_";  remark = "출하정보"	  ; suffix = String.format("%08d", nextValue); break;
	               	      
		            default:
			              prefix = "ERR"; remark = "ERROR"      ; suffix = String.format("%08d", nextValue); break;
                }
	            
	            if ( nextValue > 1 ) {
	                statement.executeUpdate("UPDATE COMMON_SEQUENCE SET MAX_ID = MAX_ID + 1 WHERE  SEQ_NAME ='"+ SeqName +"'");
	            }else {
	            	statement.executeUpdate("INSERT INTO COMMON_SEQUENCE (SEQ_NAME , MAX_ID , REMARK ) VALUES( '"+ SeqName +"', 2, '"+ remark +"' )");
	            }
                
	           result = prefix.concat(suffix);
	          
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	}
	  
	
}
