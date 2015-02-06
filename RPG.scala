package test.rpg.battle

import java.lang.Class

class RPG {
    def main(args: Array[String]){
        var Enemy = new Monster("スライム", Map(
            "HP" -> 80,
            "MP" -> 30,
            "attack" -> 35,
            "defence" -> 32, 
            "magic" -> 24,
            "speed" -> 21,
            "exp" -> 40
        ), "ポーション")
        var Hero = new Ally("勇者", Map(
            "HP" -> 130,
            "MP" -> 60,
            "attack" -> 54,
            "defence" -> 48, 
            "magic" -> 36,
            "speed" -> 44
        ))
        
        println("""
                   ∩
            　　　ノヽ
            　　／　　＼
            　 / (･)(･)∧
            　｜(ヽ＿ノ)｜
            　 ＼二二二／
        　       """)
        println("""
        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━┓
        ┃ """+ Enemy.name + """が現れた！       ┃
        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━┛
        """)
        Turn()
        
        def Turn() {
            if(Enemy.speed > Hero.speed) {
                Enemy.act(
                    (Math.random()*4).toInt
                    )
                println(Hero.name + "はどうする？")
                println("1.攻撃  2.防御  3.魔法  4.逃げる")
                Hero.act()
            }else{
                println(Hero.name + "はどうする？")
                println("1.攻撃  2.防御  3.魔法  4.逃げる")
                Hero.act()
                Enemy.act(
                    (Math.random()*4).toInt
                    )
                }
            if(Enemy.isDefending) Enemy.isDefending = false
            if(Hero.isDefending) Hero.isDefending = false
            if(Enemy.HP>0 && Hero.HP>0) Turn() 
        }
    }
    
    class Fighter(Name:String, Settings:Map[String, Int]){
        val name = Name
        var HP = Settings.getOrElse("HP", 100)
        var MP = Settings.getOrElse("MP", 60)
        var attack = Settings.getOrElse("attack", 40)
        var defence = Settings.getOrElse("defence", 40)
        var magic = Settings.getOrElse("magic", 40)
        var speed = Settings.getOrElse("speed", 40)
        var isDefending = false
        
        def attackedBy(Enemy:Fighter):(Int, Boolean) = {
            val Me = this
            val C = if(Enemy.isDefending) 0.7 else 1.0
            val isCritical = Math.random() > 0.9
            val criticalC = if(isCritical) 2.0 else 1.0
            val damage = (Me.attack - Enemy.defence/2) * criticalC * C
            (damage.toInt, isCritical)
        }
        
        def act(n:Int, Enemy:Fighter){
            var Me = this
            n match{
                case 1 => 
                    println(Me.name + "の攻撃！")
                    val damage = Enemy.attackedBy(Me)._1
                    val isCritical = Enemy.attackedBy(Me)._2
                    if(isCritical) println("会心の一撃！")
                    println(Enemy.name + "に" + damage + "のダメージ！")
                    Enemy.HP -= damage
                case 2 => Me.isDefending = true
                case 3 =>  Enemy.HP -= Enemy.attackedBy(Me)._1
                case 4 => if(Me.succeedRun) true
                case _ => true
            }
            if(Enemy.HP<=0){
                println(Enemy.name + "は倒れた！")
                if(Enemy.getClass.getName == "Monster"){
                    //println(Enemy.exp + "の経験値を手に入れた！")
                    /*if(Math.random > 0.5){
                        println("アイテム: " + Enemy.item + "を手に入れた！")
                    }*/
                }
            }
        }        
        def succeedRun:Boolean = if(Math.random() > 0.6) true else false
    }
    
    class Monster(Name:String, Settings:Map[String, Int], item:String) extends Fighter(Name:String, Settings:Map[String, Int]) {
        val exp = Settings.getOrElse("exp", 10)    
    }
    
    class Boss extends Monster {
    
    
    }
    
    class Ally(Name: String, Settings: Map[String,Int]) extends Fighter(Name: String, Settings: Map[String,Int]) {
        
    }
    
    
}