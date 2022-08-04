package com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy

import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.DynamicMovingObject

/**
 * <적 개체의 base>
 *     기본적으로 화면에서 동적으로 등장하므로 부모 클래스는 DynamicObject.
 *     모든 적의 최초 위치 y값은 0이며, y 방향으로 움직인다.
 *     따라서 y 속도(velY)만을 받아와서 위치 갱신에 활용한다.
 */
abstract class Enemy(identification: Long, sx: Float): DynamicMovingObject(identification, sx, 0F, 0F, 1F) {
    abstract val speed:Int
    abstract var health:Int

    /* 적은 y 방향으로만 속도를 가지고 위차가 변함 */
    override fun positionUpdate() {
        y += velocityY * speed
    }

    /* 적이 탄환의 power 만큼 데미지를 입음 */
    fun enemyGotDamaged(power: Int){
        health -= power
    }

    /* 적이 죽었는지 판단해서 알려줌 */
    fun isEnemyDead(): Boolean{
        return (health <= 0)
    }
}