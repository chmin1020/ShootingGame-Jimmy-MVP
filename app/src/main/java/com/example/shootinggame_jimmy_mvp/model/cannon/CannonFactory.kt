package com.example.shootinggame_jimmy_mvp.model.cannon

import com.example.shootinggame_jimmy_mvp.model.Type
import com.example.shootinggame_jimmy_mvp.model.cannon.Cannon
import com.example.shootinggame_jimmy_mvp.model.cannon.HeavyCannon
import com.example.shootinggame_jimmy_mvp.model.cannon.LightCannon
import com.example.shootinggame_jimmy_mvp.model.cannon.NormalCannon

/* 타입에 따라 적절한 Cannon 하위 객체를 만들기 위한 공장 클래스 */
object CannonFactory {
    fun create(type: Type): Cannon {
        if (type == Type.LIGHT_CANNON)
            return LightCannon()
        if (type == Type.NORMAL_CANNON)
            return NormalCannon()
        return HeavyCannon()
    }
}