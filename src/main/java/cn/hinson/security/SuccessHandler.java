package cn.hinson.security;

import cn.hinson.domain.SysUser;
import cn.hinson.util.TokenUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Created by xinshengshu on 2018/9/25.
 */
@Component
public class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final TokenUtils tokenUtils;

    private final ObjectMapper objectMapper;

    private final JsonNodeFactory jsonNodeFactory;

    protected final Log logger = LogFactory.getLog(this.getClass());

    public SuccessHandler(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
        this.objectMapper = new ObjectMapper();
        this.jsonNodeFactory = JsonNodeFactory.instance;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

//        logger.info(authentication.getName());
//        response.setStatus(HttpStatus.OK.value());
//        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//
//        try (Writer writer = response.getWriter()){
//            JsonNode jsonNode = jsonNodeFactory.objectNode()
//                .put("token",tokenUtils.generateToken(authentication));
//            objectMapper.writeValue(writer,jsonNode);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        HttpSession session = request.getSession();
        SysUser authUser = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        session.setAttribute("username", authUser.getUsername());
        session.setAttribute("authorities", authentication.getAuthorities());

        //set our response to OK status
        response.setStatus(HttpServletResponse.SC_OK);

        //since we have created our custom success handler, its up to us to where
        //we will redirect the user after successfully login
        response.sendRedirect("/");
    }
}