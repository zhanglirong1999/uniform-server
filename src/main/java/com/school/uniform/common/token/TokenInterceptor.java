package com.school.uniform.common.token;

import com.google.gson.Gson;
import com.school.uniform.common.CONST;
import com.school.uniform.common.annotation.TokenRequired;
import com.school.uniform.model.dto.Response;
import com.school.uniform.model.dto.WebResponseEnum;
import com.school.uniform.util.TokenUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * token拦截器，做token校验用
 *
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TokenRequired tokenRequired = this.queryRequestAcl(request, handler);
        if (null == tokenRequired) {
            return true;
        } else {
            return checkToken(request, response, tokenRequired);
        }
    }

    TokenRequired queryRequestAcl(HttpServletRequest request, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        TokenRequired tokenRequired = (TokenRequired) handlerMethod.getMethod().getAnnotation(TokenRequired.class);
        if (tokenRequired == null) {
            tokenRequired = (TokenRequired) handlerMethod.getBeanType().getAnnotation(TokenRequired.class);
        }
        return tokenRequired;
    }

    Boolean checkToken(HttpServletRequest request, HttpServletResponse response, TokenRequired tokenRequired) throws Exception {
        String token = request.getHeader(TokenUtil.TOKEN_HEADER);

        if (!TokenUtil.checkToken(token)) {
            response.reset();

            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain;charset=UTF-8");

            PrintWriter pw = response.getWriter();

            pw.write(new Gson().toJson(
                    new Response(WebResponseEnum.tokenError)
            ));

            pw.flush();
            pw.close();
//            response.sendError(401);
            return false;
        } else {
            /**
             * 之后通过request.getAttribute(AccountId)获取
             */
            request.setAttribute(CONST.ACL_ACCOUNTID, TokenUtil.getAccountId(token));
        }
        return true;
    }

}
