package com.example.shootinggame_jimmy_mvp.model

/* 객체 생성 및 뷰 생성 시 각 객체를 구분하기 위한 타입 상수를 담는 열거 클래스 */
enum class Type {
    LIGHT_CANNON, NORMAL_CANNON, HEAVY_CANNON, //Cannon 타입
    LIGHT_BULLET, NORMAL_BULLET, HEAVY_BULLET, //Bullet 타입
    LIGHT_ENEMY, NORMAL_ENEMY, HEAVY_ENEMY, //Enemy 타입
}

/* 난수 생성을 통한 타입 선택을 대비하여 각 타입 종류의 개수를 담는 열거 클래스 */
enum class TypeCnt(val cnt: Int){
    CANNON_TYPE(3), //Cannon 타입
    ENEMY_TYPE(3) //Enemy 타입
}