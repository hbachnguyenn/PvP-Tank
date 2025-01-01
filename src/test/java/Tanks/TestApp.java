package Tanks;

import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;


public class TestApp {

    // Test when the user presses a key, then the tank should start moving
    @Test
    public void testTankMovement() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000); // to give time to initialise stuff before drawing begins


        app.keyCode = 37;
        Tank currentTank = app.process.getPlayer();
        assertEquals(64, currentTank.getX());
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 37));
        app.delay(10);
        assertEquals(62 ,currentTank.getX());
        assertEquals(249, currentTank.getFuel());
        assertEquals(app.process.getCurrentSetting().getLayout().getTerrainList().get(62).getY(), currentTank.getY());


        app.keyCode = 39;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));
        app.delay(10);
        assertEquals(64 ,currentTank.getX());
        assertEquals(248, currentTank.getFuel());
        assertEquals(app.process.getCurrentSetting().getLayout().getTerrainList().get(64).getY(), currentTank.getY());


        app.keyCode = 38;
        while (currentTank.getAngle() != Math.PI) {
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 38));
        }
        app.delay(10);
        float[] turret = currentTank.getTankTurret();
        assertEquals(64, turret[0]);
        assertEquals(app.process.getCurrentSetting().getLayout().getTerrainList().get(64).getY() - 4, turret[1]);
        assertEquals(49, turret[2]);
        assertEquals(app.process.getCurrentSetting().getLayout().getTerrainList().get(64).getY() - 4, turret[3]);


        app.keyCode = 40;
        while (currentTank.getAngle() != 0) {
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 40));
        }
        app.delay(10);
        turret = currentTank.getTankTurret();
        assertEquals(64, turret[0]);
        assertEquals(app.process.getCurrentSetting().getLayout().getTerrainList().get(64).getY() - 4, turret[1]);
        assertEquals(79, turret[2]);
        assertEquals(app.process.getCurrentSetting().getLayout().getTerrainList().get(64).getY() - 4, turret[3]);

        app.process.getPlayer().setFuel(10000);
        app.keyCode = 37;
        for (int i = 0; i < 100; i++) {
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 37));
        }
        app.keyCode = 39;
        for (int i = 0; i < 1000; i++) {
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));
        }
        app.process.getPlayer().setFuel(-app.process.getPlayer().getFuel());
        assertEquals(0, app.process.getPlayer().getFuel());
        app.keyCode = 37;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 37));
        app.keyCode = 39;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));

        app.process.getPlayer().move("aaaa", 87);
        app.process.getPlayer().rotateTurret("aaa");
    }

    // Test when the user presses a key, then the powerup activates
    @Test
    public void testPowerup() {

        // Create a simple implementation of the App class
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000); // to give time to initialise stuff before drawing begins
        //check powerup had an effect

        //assert scoreboard value decreased as expected
        //Test increase power
        app.keyCode = 87;
        Tank currentTank = app.process.getPlayer();
        assertEquals(50, currentTank.getPower());
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'W', 87));
        app.delay(10);
        assertEquals(51 ,currentTank.getPower());

        //Test decrease power
        app.keyCode = 83;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'S', 83));
        app.delay(10);
        assertEquals(50, currentTank.getPower());

        //Test teleport
        app.keyCode = 84;
        app.process.getPlayer().setScore(300);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'T', 84));
        app.delay(2000);
        Teleport teleport = app.process.getPlayer().getTeleport();
        assertTrue(teleport.isChoosingLocation());
        assertFalse(teleport.completedTeleport());
        assertEquals(teleport.getX(), currentTank.getX());
        assertEquals(teleport.getY(), currentTank.getY());
        app.keyCode = 39;
        for (int i = 0; i < 7; i++) {
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));
        }
        app.delay(1000);
        app.keyCode = 37;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 37));
        app.delay(2000);
        assertEquals(teleport.getX(), 70);
        assertEquals(teleport.getY(), app.process.getCurrentSetting().getLayout().getTerrainList().get(70).getY());
        app.keyCode = 84;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'T', 84));
        assertFalse(teleport.isChoosingLocation());
        assertFalse(teleport.completedTeleport());
        assertEquals(teleport.getX(), currentTank.getX());
        assertEquals(teleport.getY(), currentTank.getY());
        assertEquals(285, currentTank.getScore());

        app.keyCode = 84;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'T', 84));
        app.delay(100);
        for(int i = 0; i < 200; i++) {
            app.keyCode = 37;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 37));
        }

        for(int i = 0; i < 900; i++) {
            app.keyCode = 39;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));
        }
        app.keyCode = 84;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'T', 84));
        assertEquals(270, app.process.getPlayer().getScore());

        //Test buy health
        currentTank.setHealth(-45);
        assertEquals(55, currentTank.getHealth());
        app.keyCode = 82;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'R',82));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'R', 82));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'R', 82));
        assertEquals(210, currentTank.getScore());
        assertEquals(100, currentTank.getHealth());

        //Test shield
        app.keyCode = 72;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'H', 72));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'H', 72));
        assertEquals(190, currentTank.getScore());
        currentTank.disableShield();
        assertFalse(currentTank.getShield());

        //Test buy fuel
        app.keyCode = 70;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'S', 70));
        assertEquals(180, currentTank.getScore());
        assertEquals(450, currentTank.getFuel());

        //Test all when score = 0
        app.process.getPlayer().setScore(-app.process.getPlayer().getScore());
        assertEquals(0, app.process.getPlayer().getScore());
        app.keyCode = 70;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'S', 70));
        app.keyCode = 72;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'H', 72));
        app.keyCode = 84;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'T', 84));
    }
    @Test
    public void testProgress() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000);

        //Switch player
        app.keyCode = 32;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        assertEquals('B', app.process.getPlayer().getCharacter());
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        assertEquals('C', app.process.getPlayer().getCharacter());

    }
    @Test
    public void testProjectile() throws InterruptedException {
        //perform key press action - space bar
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000);

        Wind wind = new Wind(new Random());
        int initialSpeed = wind.getSpeed();
        wind.setRandomSpeed(new Random());
        int newSpeed = wind.getSpeed();
        assertTrue(newSpeed >= (initialSpeed - 5) && newSpeed <= (initialSpeed + 5));
        

        int initialframes = app.frameCount;
        Thread.sleep(1000); //1 second passes, check game state was changed in expected way
        //projectile moves through the air
        app.keyCode = 87;
        int i = 0;
        while(i < 52) {
            app.keyPressed(new KeyEvent(null, 0, 0, 0, 'S', 87));
            i++;
        }
        i = 0;
        while (i < 105) {
            app.keyCode = 83;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, 'S', 83));
            i++;
        }

        if (app.process.getCurrentSetting().getWind().getSpeed() > 15) {
            app.keyCode = 38;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 38));
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 38));
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 38));
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 38));
            app.keyCode = 40;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 40));
        }

        if (app.process.getCurrentSetting().getWind().getSpeed() < -15) {
            app.keyCode = 40;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 40));
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 40));
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 40));
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 40));
            app.keyCode = 38;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 38));
        }

        app.keyCode = 32;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        assertEquals(4, app.process.getActiveProjectiles().size());
        assertEquals(app.process.getCurrentSetting().getLayout().getTankList().get('A').getProjectile().size(), 1);
        int parachute = app.process.getCurrentSetting().getLayout().getTankList().get('A').getParachute();
        while (true) {
            if (app.process.getCurrentSetting().getLayout().getTankList().get('A').getHealth() < 100) {
                break;
            }
        }
        assertTrue(app.process.getCurrentSetting().getLayout().getTankList().get('A').isFalling());
        app.keyCode = 37;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 37));
        app.keyCode = 39;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));
        app.delay(3000);
        assertEquals(app.process.getCurrentSetting().getLayout().getTankList().get('A').getParachute(), parachute - 1);
        assertTrue(app.process.getCurrentSetting().getLayout().getTankList().get('A').getHealth() < 100);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));


        int endframes = app.frameCount;
        //do we expect the projectile to have hit the target? - if time is too short, then assert projectile still exists

        //if expected projectile to have hit terrain - assert terrain is destroyed

        //however, the user has to wait 1 second for the testcase to complete
        // - to avoid this - use app.noLoop()

        //simulate 3 second of time in the game
        for (i = 0; i < App.FPS*3; i++) {
            app.draw(); //simulate frame
            //or if a seperate method is used for game logic tick, eg. app.tick()
        }
    }

    @Test
    public void testFalling() {
        App app = new App();
        app.configPath = "test1.json";
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000);

        app.process.getCurrentSetting().getLayout().getTankList().get('B').setParachute(-2);
        app.process.getCurrentSetting().getLayout().getTankList().get('B').setParachute(-1);
        assertEquals(0, app.process.getCurrentSetting().getLayout().getTankList().get('B').getParachute());
        while (app.process.getPlayer().getPower() < 100) {
            app.keyCode = 87;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, 'W', 87));
        }
        while (app.process.getPlayer().getAngle() != Math.PI) {
            app.keyCode = 38;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 38));
        }
        app.keyCode = 32;
        assertEquals(1, app.process.getCurrentRound());
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        app.delay(3000);


    }

    @Test
    public void testEndGame() {
        App app = new App();
        app.configPath = "test2.json";
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000);

        while (app.process.getPlayer().getPower() < 100) {
            app.keyCode = 87;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, 'W', 87));
        }
        while (app.process.getPlayer().getAngle() > Math.PI/6) {
            app.keyCode = 40;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 40));
        }

        app.keyCode = 32;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        app.delay(6000);
        app.keyCode = 32;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        while (app.process.getPlayer().getAngle() < 3*Math.PI/4) {
            app.keyCode = 38;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 38));
        }
        app.keyCode = 32;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        app.delay(2000);
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        app.delay(500);


        app.process.getCurrentSetting().getLayout().getTankList().get('F').setHealth(-90);

        while (app.process.getPlayer().getAngle() != Math.PI) {
            app.keyCode = 38;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 38));
        }
        app.keyCode = 32;
        assertEquals(1, app.process.getCurrentRound());
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        app.delay(3000);
        assertEquals(2, app.process.getCurrentRound());
        app.process.getCurrentSetting().getLayout().getTankList().get('F').setHealth(-80);
        while (app.process.getPlayer().getPower() < 100) {
            app.keyCode = 87;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, 'W', 87));
        }
        while (app.process.getPlayer().getAngle() != Math.PI) {
            app.keyCode = 38;
            app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 38));
        }

        app.keyCode = 32;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        app.delay(3000);

        app.keyCode = 32;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 32));
        app.keyCode = 37;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 37));
        app.keyCode = 38;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 38));
        app.keyCode = 39;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 39));
        app.keyCode = 40;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, ' ', 40));
        app.keyCode = 115;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 's', 115));
        app.keyCode = 119;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'w', 119));
        app.keyCode = 116;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 't', 116));
        app.keyCode = 104;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'h', 104));
        app.keyCode = 114;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'r', 114));
        app.keyCode = 90;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'z', 90));
        app.keyCode = 70;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'z', 70));
        app.keyCode = 102;
        app.keyPressed(new KeyEvent(null, 0, 0, 0, 'z', 102));
    }

}

// gradle run						Run the program
// gradle test						Run the testcases

// Please ensure you leave a comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report:
// gradle test jacocoTestReport