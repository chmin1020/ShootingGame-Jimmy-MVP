package com.example.shootinggame_jimmy_mvp.model.cannon

//NormalCannon -> 체력이 2, 추진력이 2인 대포 객체
class NormalCannon: Cannon() {
    override var life = 2
    override val cannonPower = 2
}