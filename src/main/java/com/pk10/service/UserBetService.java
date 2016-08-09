package com.pk10.service;

import java.util.List;

import com.pk10.bean.UserBet;
import com.pk10.bean.UserInfo;

public interface UserBetService extends BaseService<UserBet> {

	List<UserBet> getUserBetByOpenid(UserInfo userInfo) throws Exception;

	Integer saveList(List<UserBet> userBets) throws Exception;

	UserBet getOneByIdnum(UserBet userBet) throws Exception;
}
