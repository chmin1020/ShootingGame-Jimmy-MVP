package com.example.shootinggame_jimmy_mvp.model.cannon

//HeavyCannon -> 체력이 4, 추진력이 1인 대포 객체
class HeavyCannon: Cannon() {
    override var life = 4
    override val cannonPower = 1
}