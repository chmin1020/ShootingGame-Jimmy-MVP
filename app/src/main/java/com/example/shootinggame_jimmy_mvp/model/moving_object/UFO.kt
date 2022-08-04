package com.example.shootinggame_jimmy_mvp.model.moving_object

/**
 * <enemy 생성 위치를 결정하는 포탈 클래스>
 *     포탈은 y좌표 -> 0으로 고정하고 가로 기준 화면 끝에서 끝으로 왔다갔다 한다.
 *     따라서 가로 화면의 범위를 정하고, 끝부분에 갈 때마다 방향을 전환한다.
 */
class UFO(widthRange: Int): MovingObject(2F, 0F, widthRange * 0.01F, 0F){
    //이동 가능 범위
    private val atLeast = 2F
    private val atMost = widthRange * 0.98F

    //방향은 끝에 갈 때마다 바뀜
    private var direct = 1

    /* 적은 x 방향으로만 속도를 가지고 위치가 변함
       화면의 끝으로 가면 방향을 반대로 바꾸는 작업도 필요함 */
    override fun positionUpdate() {
        x += velocityX * direct
        if(x !in atLeast..atMost)
            direct *= -1
    }
}