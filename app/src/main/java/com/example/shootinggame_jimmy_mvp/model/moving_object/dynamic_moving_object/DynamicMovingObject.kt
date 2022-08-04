package com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object

import com.example.shootinggame_jimmy_mvp.model.Type
import com.example.shootinggame_jimmy_mvp.model.moving_object.MovingObject

/**
 * <동적으로 생성되는 MovingObject(Bullet, Enemy)를 위한 추상 클래스>
 *     생성과 삭제를 반복해야 하는 만큼 객체의 종류를 알 수 있는 type,
 *     같은 종류 중에서도 각 객체를 구별할 수 있는 id를 프로퍼티로 가짐
 */
abstract class DynamicMovingObject(identifier: Long, sx: Float, sy:Float, vx: Float, vy: Float): MovingObject(sx, sy, vx, vy) {
    //객체의 세부 식
    val id = identifier

    //타입은 자식 클래스마다 다르게 정하는 값이므로 abstract
    abstract val type: Type
}