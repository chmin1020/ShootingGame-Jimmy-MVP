package com.example.shootinggame_jimmy_mvp.model.cannon

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * <대포 개체의 base>
 *     대포는 움직이지 않으나, 회전을 하며 탄환의 초기화에 큰 영향을 준다.
 *     따라서 회전 및 목숨과 관련한 값(각도, 벡터, 목숨)과 함수만을 가진다.
 */
abstract class Cannon {
    //삼각함수로 벡터를 구하기 위해서 사용되는 상수값
    private val radianCal = PI / 180

    //변하는 변수값 (각도, 벡터, 목숨)
    var degree: Float = 0F
        private set
    var xVector: Float = 0F
        private set
    var yVector: Float = -1F
        private set

    //각 세부 대포들에서 결정되는 변수들
    protected abstract val cannonPower: Int
    abstract var life: Int
        protected set


    /* 대포 회전 시 변경되는 각도를 적용하고, 그에 따라 벡터를 변경하는 함수 */
    fun rotate(status: Int): Float {
        degree = 1.8F * status - 90F
        val radian = degree * radianCal
        xVector = sin(radian).toFloat()
        yVector = -cos(radian).toFloat()
        return degree
    }

    /* 이 대포에서 발사되는 탄환에게 부여할 속도 */
    fun shootVelocityX(): Float{
        return cannonPower * xVector
    }
    fun shootVelocityY(): Float{
        return cannonPower * yVector
    }

    /* 적을 놓칠 때마다 대포의 life 감소 */
    fun lifeDecrease(){
        life -= 1
    }
}