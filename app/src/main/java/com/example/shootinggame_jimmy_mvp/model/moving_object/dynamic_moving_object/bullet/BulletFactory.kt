package com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet

import com.example.shootinggame_jimmy_mvp.model.Type
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet.Bullet
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet.HeavyBullet
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet.LightBullet
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet.NormalBullet

/* 타입에 따라 적절한 Bullet 하위 객체를 만들기 위한 공장 클래스 */
object BulletFactory {
    fun create(type: Type, id: Long, sx: Float, sy: Float, vx: Float, vy: Float): Bullet {
        if(type == Type.LIGHT_BULLET)
            return LightBullet(id, sx, sy, vx, vy)
        if(type == Type.NORMAL_BULLET)
            return NormalBullet(id, sx, sy, vx, vy)
        return HeavyBullet(id, sx, sy, vx, vy)
    }
}