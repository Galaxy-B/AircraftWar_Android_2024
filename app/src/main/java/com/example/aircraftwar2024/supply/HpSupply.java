package com.example.aircraftwar2024.supply;


import com.example.aircraftwar2024.basic.aircraft.HeroAircraft;
import com.example.aircraftwar2024.game.BaseGame;

/**
 * 加血道具：增加HP值。
 * 自动触发
 *
 * @author hitsz
 */
public class HpSupply extends AbstractFlyingSupply {
    private int increasedHp = 30;

    public HpSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate() {
        HeroAircraft.getHeroAircraft().increaseHp(increasedHp);

        BaseGame.mySoundPool.playMusic(3);
        System.out.println("HpSupply active");
    }

}
