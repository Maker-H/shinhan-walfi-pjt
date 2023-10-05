package com.shinhan.walfi.mapper;

import com.shinhan.walfi.dao.DailyQuestDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface QuestMapper {

    void initializeDailyQuest();

    List<DailyQuestDao> readRandomDailyQuestByLimit(int limit);

    List<DailyQuestDao> getUserDailyQuest(String user_id);

    void increaseSpecificPerformedQuest(@Param("user_id") String user_id,
                                        @Param("quest_id") long quest_id);

    boolean checkQuestIsCompleted(@Param("user_id") String user_id,
                                  @Param("quest_id") long quest_id);

    boolean checkIFQuestIsComplete(@Param("user_id") String user_id,
                                   @Param("quest_id") long quest_id);

    String findUserIdByMainAccount(String MainAccountNumber);

    void updateQuestStatusTrue(@Param("user_id") String user_id,
                               @Param("quest_id") long quest_id);

    void updateQuestStatus(@Param("user_id") String user_id,
                           @Param("quest_id") long quest_id,
                           @Param("status") int status);
}
