package com.pk10.service;

import com.pk10.bean.BetInit;

import java.util.List;

/**
 * Created by ron on 16-9-3.
 */
public interface BetInitService {

	public Integer saveBetInit(BetInit betInit);

    public List<BetInit> getAllBetInit();

    public int updateBetInit(BetInit betInit);

    public int updateBetInitMoney(BetInit betInit);

    public List<BetInit> getBetInitByName(BetInit betInit);

    public BetInit getOneBetInitByName(BetInit betInit);

    public List<BetInit> getAllGname();

    public List<BetInit> getAllGameTypeByGname(String gName);

    public BetInit getBetinitByNameAndType(BetInit betInit);

    public BetInit getGameInitMoney(String gName);
}
