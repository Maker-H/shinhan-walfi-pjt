package com.shinhan.walfi.aop;

import com.shinhan.walfi.domain.HttpResult;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.mapper.QuestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class QuestAspect {

    private final QuestMapper questMapper;

    private long aa(String status) {

        long returnValue = 0L;

        switch (status) {
            case "밥먹기 완료":
                returnValue = 3L;
                break;
            case "훈련하기 완료":
                returnValue = 4L;
                break;
        }

        return returnValue;
    }

    @AfterReturning(
            pointcut = "execution(* com.shinhan.walfi.controller.BankController.*(..)) || " +
                    "execution(* com.shinhan.walfi.controller.CharacterController.*(..))",
            returning = "result"
    )
    public void conductQuest(JoinPoint joinPoint, ResponseEntity<HttpResult> result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Bank Service : {}", methodName);

        String userId = null;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userId = ((User) authentication.getPrincipal()).getUserId();
        } catch (NullPointerException | ClassCastException e) {
            log.info("JWT 검증 안 돼서, Quest 수행 횟수 증가 실패");
            return;
        }
        System.out.println("UserId = " + userId);

        long questIdx = 0L;
        switch (methodName) {
            case "localTransfer":
                questIdx = 1L;
                break;
            case "changeCharacterStatus":
                questIdx = aa(result.getBody().getMessage());
                break;
        }

        if (questIdx != 0) {
            questMapper.increaseSpecificPerformedQuest(userId, questIdx);
            boolean isCompleted = questMapper.checkIFQuestIsComplete(userId, questIdx);

            if (isCompleted) {
                log.info("{} completed {} quest", userId, questIdx);
                questMapper.updateQuestStatusTrue(userId, questIdx);
            }
        }
    }
}
