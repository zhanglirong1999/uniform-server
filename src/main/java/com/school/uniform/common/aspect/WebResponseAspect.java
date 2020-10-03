package com.school.uniform.common.aspect;

import com.school.uniform.model.dto.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class WebResponseAspect {
    @Pointcut("@within(com.school.uniform.common.annotation.WebResponse)")
    private void responseCut() { }

    @Around("responseCut()")
    public Object responseCut(ProceedingJoinPoint pjp)throws Throwable {
        Object obj = pjp.proceed();
        if(obj instanceof Response){
            return obj;
        }
        return Response.success(obj);

    }
}
