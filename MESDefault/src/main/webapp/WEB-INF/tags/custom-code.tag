<%@ tag import="org.apache.commons.lang3.StringUtils" %>
<%@ tag import="java.util.List" %>
<%@ tag import="java.util.LinkedHashMap" %>
<%@ tag import="com.delcam.utils.CustomCodeUtils" %>
<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="groupCd" required="true" %>
<%@ attribute name="name" required="false" %>
<%@ attribute name="clazz" required="false" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="dataPath" required="false" %>
<%@ attribute name="type" required="false" %>
<%@ attribute name="defaultValue" required="false" %>
<%@ attribute name="emptyValue" required="false" %>
<%@ attribute name="emptyText" required="false" %>

<%
    if (StringUtils.isEmpty(type)) {
        type = "select";
    }
    StringBuilder builder = new StringBuilder();
    LinkedHashMap<String, String> customCode = CustomCodeUtils.getCustomCode( groupCd );

    switch (type) {
        case "select":
            builder.append("<select class=\"form-control "+ clazz +"\"  ");
            if (StringUtils.isEmpty(emptyValue)) {
                emptyValue = "";
            }
            if (StringUtils.isNotEmpty(id)) {
                builder.append("id=\"" + id + "\"");
            }
            if (StringUtils.isNotEmpty(name)) {
                builder.append("name=\"" + name + "\"");
            }
            if (StringUtils.isNotEmpty(dataPath)) {
                builder.append("data-ax-path=\"" + dataPath + "\"");
            }
            builder.append(">");
            for ( String key : customCode.keySet() ) {
          	  if (StringUtils.isNotEmpty(defaultValue) && defaultValue.equals(key)) {
                    builder.append(String.format("<option value=\"%s\" selected>%s</option>", key, customCode.get(key)));
                } else {
                    builder.append(String.format("<option value=\"%s\">%s</option>",key, customCode.get(key)));
                }
            } 		
            if (StringUtils.isEmpty(defaultValue) && StringUtils.isNotEmpty(emptyText)) {
            	if(customCode.isEmpty()){
            	   builder.append(String.format("<option value=\"%s\">%s</option>", emptyValue, emptyText));
            	}else{
            	    builder.append(String.format("<option value=\"%s\">%s</option>", "", "선택없음"));
            		//if( groupCd != "procList" ){
            		//	 builder.append(String.format("<option value=\"%s\">%s</option>", "", "선택없음"));
            		//}
              	}
            }
          
            builder.append("</select>");
            break;

        case "checkbox":
        	  for ( String key : customCode.keySet() ) {
                if (StringUtils.isNotEmpty(defaultValue) && defaultValue.equals(key)) {
                    builder.append(String.format("<label class=\"checkbox-inline\"><input type=\"checkbox\" name=\"%s\" data-ax-path=\"%s\" value=\"%s\" checked> %s </label>", name, dataPath, key, customCode.get(key)));
                } else {
                    builder.append(String.format("<label class=\"checkbox-inline\"><input type=\"checkbox\" name=\"%s\" data-ax-path=\"%s\" value=\"%s\"> %s </label>", name, dataPath, key, customCode.get(key)));
                }
        	  } 
            break;

        case "radio":
        	 for ( String key : customCode.keySet() ) {
                if (StringUtils.isNotEmpty(defaultValue) && defaultValue.equals(key)) {
                    builder.append(String.format("<label class=\"checkbox-inline\"><input type=\"radio\" name=\"%s\" data-ax-path=\"%s\" value=\"%s\" checked> %s </label>", name, dataPath, key,  customCode.get(key)));
                } else {
                    builder.append(String.format("<label class=\"checkbox-inline\"><input type=\"radio\" name=\"%s\" data-ax-path=\"%s\" value=\"%s\"> %s </label>", name, dataPath, key,  customCode.get(key)));
                }
        	 }
            break;
    }
%>

<%=builder.toString()%>