package com.example.shootinggame_jimmy_mvp.model.cannon

//LightCannon -> 체력이 1, 추진력이 4인 대포 객체
class LightCannon: Cannon() {
    override var life = 1
    override val cannonPower = 4
}