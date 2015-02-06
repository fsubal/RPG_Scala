package test.rpg.battle

import java.lang.Class

object RPG {
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
            "attack" -> 44,
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
        println("""
        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━┓
        ┃ Slime: """+ Enemy.HP + """   Hero: """+ Hero.HP + """     ┃
        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━┛
        """)
        Turn()
        
        def Turn() {
            if(Enemy.speed > Hero.speed) {
                if(Enemy.HP>0){
                    Enemy.act( (Math.random()*4).toInt, Hero )
                    if(Hero.HP>0){
                    println(Hero.name + "はどうする？")
                    println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓")
                    println("┃ 1.攻撃  2.防御  3.魔法  4.逃げる  ┃")
                    println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛")
                    Hero.act( (Math.random()*4).toInt, Enemy)
                    }
                }
                
            }else{
                if(Hero.HP>0){
                    println(Hero.name + "はどうする？")
                    println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓")
                    println("┃ 1.攻撃  2.防御  3.魔法  4.逃げる  ┃")
                    println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛")
                    Hero.act( (Math.random()*4).toInt, Enemy)
                    if(Enemy.HP>0){
                        Enemy.act( (Math.random()*4).toInt, Hero )
                    }
                }
            }
            if(Enemy.isDefending) Enemy.isDefending = false
            if(Hero.isDefending) Hero.isDefending = false
            if(Enemy.HP>0 && Hero.HP>0){
                println("""
        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━┓
        ┃ Slime: """+ Enemy.HP + """   Hero: """+ Hero.HP + """     ┃
        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━┛
        """)
                Turn()
            }
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
        
        def attackedBy(Attacker:Fighter):(Int, Boolean) = {
            val Me = this
            val C = if(Me.isDefending) 0.5 else 1.0
            val isCritical = Math.random() > 0.9
            val criticalC = if(isCritical) 2.0 * Ranging else 1.0
            val damage = (Attacker.attack - Me.defence/2) * criticalC * C * Ranging
            (damage.toInt, isCritical)
        }
        
        def magickedBy(Magicker:Fighter):Int = {
            val Me = this
            val C = if(Me.isDefending) 0.5 else 1.0
            val damage = (Magicker.magic - Me.magic/2) * C * Ranging
            damage.toInt
        }
        
        def act(n:Int, Enemy:Fighter){
            var Me = this
            n match{                    
                case 2 => 
                    println(Me.name + "は防御している...")
                    Me.isDefending = true
                case 3 => 
                    println(Me.name + "は魔法を唱えた！")
                    val damage = Enemy.magickedBy(Me).toInt
                    Me.MP -= 5
                    println(Enemy.name + "に" + damage + "のダメージ！")
                    Enemy.HP -= damage
                //case 4 => if(Me.succeedRun) true
                case _ => 
                    println(Me.name + "の攻撃！")
                    val damage = Enemy.attackedBy(Me)._1
                    val isCritical = Enemy.attackedBy(Me)._2
                    if(isCritical) println("会心の一撃！")
                    println(Enemy.name + "に" + damage + "のダメージ！")
                    Enemy.HP -= damage
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
    
    class Boss(Name: String, Settings: Map[String,Int], item:String)
    extends Monster(Name: String, Settings: Map[String,Int], item:String) {
    
    
    }
    
    class Ally(Name: String, Settings: Map[String,Int]) extends Fighter(Name: String, Settings: Map[String,Int]) {
        
    }
    
    def Ranging:Double = 1 - (Math.random()*0.2 - 0.1)
    
}