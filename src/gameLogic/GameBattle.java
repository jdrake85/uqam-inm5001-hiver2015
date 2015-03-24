/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import gameLogic.gameboard.GameBoard;
import gameLogic.gameboard.Tile;
import gameLogic.pathfinding.Coordinates;
import gameLogic.pathfinding.CoordPath;
import gameLogic.pathfinding.OptimalPaths;
import gameLogic.skills.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author User
 */
public class GameBattle {

    private Creature creaturePlayingTurn; // TODO: Not used or implemented yet
    private List<Creature> creatureList; // Fast access to creatures on gameboard
    private OptimalPaths paths;
    private GameBoard gameboard;
    private boolean[][] currentOverlay = null;
    private PriorityQueue<CreatureSpeedTurnTriplet> creaturePriority = new PriorityQueue<CreatureSpeedTurnTriplet>();
    private int maxCumulativeCreatureSpeed = 0;
    private int minCumulativeCreatureSpeed = Integer.MAX_VALUE;
    private int turnCounter = 1;
    private boolean gameOver = false;

    public GameBattle() {
        paths = new OptimalPaths(8, 8);
        gameboard = new GameBoard();
    }

    public boolean[][] getCalculatedOverlayForCreatureSkill(Creature creature, int skillNumber) {
        Skill skill = creature.prepareSkill(skillNumber);
        Coordinates originatingCoords = gameboard.getCreatureCoordinates(creature);
        skill.setOriginatingFrom(originatingCoords);
        return gameboard.getSkillOverlay(skill);
    }

    public boolean[][] getCalculatedOverlayForCreatureMoves(Creature creature) {
        calculateOptimalPathsForCreature(creature);
        int stepCount = creature.maximumStepsAbleToWalk();
        return paths.getTilesReachableInAtMostNSteps(stepCount);
    }

    private void calculateOptimalPathsForCreature(Creature creature) {
        Coordinates initCoords = gameboard.getCreatureCoordinates(creature);
        boolean[][] occupiedTiles = gameboard.getOccupiedTiles();
        paths.calculateOptimalPathsStartingFromCoordinates(occupiedTiles, initCoords);
    }

    public void displayOptimalPaths() {
        paths.displayOptimalPaths();
    }

    public void refreshCreatureList() {
        gameboard.removeDeadCreatures();
        creatureList = gameboard.getFullCreatureList();
        //System.out.println("Refreshing list of ALL creatures on the map: " + creatureList);
    }

    public boolean containsGoodCreatures() {
        List<Creature> goodCreatures = gameboard.getGoodCreatureList();
        return !goodCreatures.isEmpty();
    }

    public boolean goodCreatureHasBeenDefeated() {
        boolean defeat = false;
        for (Creature creature : creatureList) {
            if (creature.isGood() && !creature.isAlive()) {
                defeat = true;
                break;
            }
        }
        return defeat;
    }

    public boolean containsBadCreatures() {
        List<Creature> badCreatures = gameboard.getBadCreatureList();
        return !badCreatures.isEmpty();
    }

    public boolean containsCreature(Creature targetCreature) {
        refreshCreatureList(); //precaution
        boolean creatureFound = false;
        for (Creature creature : creatureList) {
            if (creature.equals(targetCreature)) {
                creatureFound = true;
                break;
            }
        }
        return creatureFound;
    }

    public void displayCreatureCoordinates(Creature creature) {
        gameboard.displayCreatureCoordinates(creature);
    }

    public void draw() {
        currentOverlay = null;
        gameboard.draw();
    }

    public void drawWithOverlayForCreatureMoves(Creature creature) {
        currentOverlay = getCalculatedOverlayForCreatureMoves(creature);
        gameboard.draw(currentOverlay);
    }

    public void drawWithOverlayForCreatureSkill(Creature creature, int skillNumber) {
        currentOverlay = getCalculatedOverlayForCreatureSkill(creature, skillNumber);
        gameboard.draw(currentOverlay);
    }

    public void insertCreatureAt(Creature creature, int xCoord, int yCoord) {
        gameboard.insertCreatureAt(creature, xCoord, yCoord);
        creature.displayCreatureOn3DBoard(xCoord, yCoord);
        refreshCreatureList();
    }

    // Quick implementation
    public void removeCreatureAt(int xCoord, int yCoord) {
        Tile tile = gameboard.getTileAt(xCoord, yCoord);
        tile.removeOccupier();
        //TODO creature.hideCreatureOn3DBoard();
    }

    // Unit movement
    public void moveCreatureInDirection(Creature creature, String direction) {
        if (creatureCanMoveInDirection(creature, direction)) {
            creature.consumeEnergyForSteps(1);
            gameboard.moveCreatureInDirection(creature, direction);
        } // Outputs for debugging purposes handled by Creature/Gameboard classes
    }

    // Unit movement
    public boolean creatureCanMoveInDirection(Creature creature, String direction) {
        boolean validMove = false;
        // Outputs for debugging purposes
        if (!creature.canPayEnergyCostForSteps(1)) {
            System.out.println("Error: creature does not have energy to move");
        } else { // Outputs handled by gameboard methods
            validMove = gameboard.validDestinationTile(creature, direction);
        }
        return validMove;
    }

    public MotionEvent moveCreatureTo(Creature creature, Coordinates destCoords) {
        MotionEvent motionEvent = null;
        if (creatureCanMoveTo(creature, destCoords)) {
            MotionPath path = new MotionPath();

            CoordPath pathChosen = paths.getPathForCreatureToCoordinates(creature, destCoords);
            creature.consumeEnergyForSteps(pathChosen.length() - 1);
            System.out.println(pathChosen);

            //Coordinates initCoords;
            //Coordinates nextCoords = pathChosen.popFirstCoordinates();

            while (!pathChosen.isEmpty()) {
                Coordinates coord = pathChosen.popFirstCoordinates();
                path.addWayPoint(new Vector3f(coord.getXCoord(), -1, coord.getYCoord()));
            }

            motionEvent = creature.generateMotionEventForMovingCreatureOn3DBoard(path);
            gameboard.moveCreatureTo(creature, destCoords);



            // Walk animation
            /*
             do {
                
             initCoords = nextCoords;
             nextCoords = pathChosen.popFirstCoordinates();
             gameboard.moveCreatureTo(creature, nextCoords);
             System.out.println("BATTLE(3D): Moving from " + initCoords + " to " + nextCoords);
             creature.moveCreatureOn3DBoard(initCoords.getXCoord(), initCoords.getYCoord(), nextCoords.getXCoord(), nextCoords.getYCoord());
             } while (!pathChosen.isEmpty()); */


        } else {
            System.out.println("Error: creature cannot move to " + destCoords);
        }
        return motionEvent;
    }

    private boolean creatureCanMoveTo(Creature creature, Coordinates destCoords) {
        int availableSteps = creature.maximumStepsAbleToWalk();
        return paths.coordinatesReachableInAtMostDistanceOf(destCoords, availableSteps);
    }

    public MotionEvent useCreatureSkillAt(Creature creature, int skillNumber, Coordinates coords) {
        MotionEvent attackEvent = null; //TODO: implement attack animation
        Skill skill = creature.prepareSkill(skillNumber);
        Coordinates originatingCoords = gameboard.getCreatureCoordinates(creature);
        skill.setOriginatingFrom(originatingCoords);
        if (creatureCanUseSkillAt(creature, skillNumber, coords)) {
            creature.consumeEnergyForSkillNumber(skillNumber);
            skill.setTargetCoordinates(coords);
            gameboard.performTargetedSkill(skill);
            removeDeadCreaturesFromTurnOrder();
            while (creaturePriority.size() < 5) {
                addCreatureListOnceToCreaturePriority();
            }
        } else if (!creature.canPayEnergyCostForSkillNumber(skillNumber)) {
            System.out.println("Not enough energy!");
        }
        return attackEvent;
    }

    private boolean creatureCanUseSkillAt(Creature creature, int skillNumber, Coordinates coords) {
        //System.out.println("Skill target coords: " + coords);
        Skill skill = creature.prepareSkill(skillNumber);
        currentOverlay = gameboard.getSkillOverlay(skill);
        int xCoord = coords.getXCoord();
        int yCoord = coords.getYCoord();
        return creature.canPayEnergyCostForSkillNumber(skillNumber)
                && currentOverlay[xCoord][yCoord];
    }

    public void start() {
        refreshCreatureList();
        forceGoodCreaturesToBeInitialyFastest();
        while (creaturePriority.size() < 5) {
            addCreatureListOnceToCreaturePriority();
        }

        CreatureSpeedTurnTriplet startingPair = creaturePriority.peek();
        // System.out.println("*** NEXT TURN: " + startingPair);
        System.out.println('\n' + "------------------" + '\n' + "TURN #" + turnCounter++ + '\n' + "------------------");
        creaturePlayingTurn = startingPair.getCreature();
        displayCreatureSpeedPairsForTurnOrder();
    }

    private void addCreatureListOnceToCreaturePriority() {
        for (Creature creature : creatureList) {
            addCreatureToCreaturePriorityOnce(creature);
        }
    }

    // Note: same creature can be added several times to the priority queue, each time having a higher speed value
    private void addCreatureToCreaturePriorityOnce(Creature creature) {
        creaturePriority.add(new CreatureSpeedTurnTriplet(creature));
        int cumulativeSpeed = creature.getCumulativeTurnSpeed();
        if (cumulativeSpeed > maxCumulativeCreatureSpeed) {
            maxCumulativeCreatureSpeed = cumulativeSpeed;
        } else if (cumulativeSpeed < minCumulativeCreatureSpeed) {
            minCumulativeCreatureSpeed = cumulativeSpeed;
        }
        creature.incrementTurnsAssigned();
        creature.incrementTurnSpeedAfterEndOfTurn();
    }

    private void addCreatureToCreaturePriorityRecursivelyAtLeastOnceAccordingToSpeed(Creature creature) {
        int upperLimitSpeedThreshold = maxCumulativeCreatureSpeed + (2 * creature.getOriginalSpeed());
        do {
            addCreatureToCreaturePriorityOnce(creature);
        } while (creature.getCumulativeTurnSpeed() <= upperLimitSpeedThreshold);
    }

    private void forceGoodCreaturesToBeInitialyFastest() {
        int goodCreatureCounter = 0;
        for (Creature creature : creatureList) {
            if (creature.isAlignedTo("good")) {
                creature.setCumulativeSpeed(++goodCreatureCounter);
            }
        }
    }

    public void endTurn() {
        refreshCreatureList();
        creaturePriority.poll();
        //System.out.println("*** TURN PLAYED BY: " + creaturePriority.poll()); // Pop creature who just played from priority queue
        addCreatureToCreaturePriorityRecursivelyAtLeastOnceAccordingToSpeed(creaturePlayingTurn); // Push creature who just played, who is now 'slower'
        CreatureSpeedTurnTriplet nextPair = creaturePriority.peek();
        creaturePlayingTurn = nextPair.getCreature();
        //System.out.println("*** NEXT TURN: " + nextPair);
        if (!gameOver) {
            System.out.println('\n' + "------------------" + '\n' + "TURN #" + turnCounter++ + '\n' + "------------------");
            displayCreatureSpeedPairsForTurnOrder();
        } else {
            System.out.println("------------------------------FIN---------------------------------");
        }
    }

    private void removeDeadCreaturesFromTurnOrder() {
        List<Creature> deadCreatures = null;
        for (Creature creature : creatureList) {
            if (!creature.isAlive()) {
                if (deadCreatures == null) {
                    deadCreatures = new ArrayList<Creature>();
                }
                deadCreatures.add(creature);
            }
        }
        if (deadCreatures != null) {
            for (Creature deadCreature : deadCreatures) {
                creatureList.remove(deadCreature);
                gameboard.removeDeadCreatures();
                while (creaturePriority.remove(new CreatureSpeedTurnTriplet(deadCreature))) {
                    // Do nothing
                }
            }
        }
    }

    public boolean isZombieTurn() {
        return creaturePlayingTurn.isAlignedTo("bad");
    }

    public Creature[] getCreatureTurnOrder() {
        int allCreatureCount = creatureList.size();
        Creature[] fullCreatureTurnOrder = new Creature[allCreatureCount];
        creaturePriority.toArray(fullCreatureTurnOrder);
        Arrays.sort(fullCreatureTurnOrder);
        Creature[] fiveCreatureTurnOrder = new Creature[5];
        System.arraycopy(fullCreatureTurnOrder, 0, fiveCreatureTurnOrder, 0, Math.min(5, allCreatureCount));
        for (int i = allCreatureCount; i < 5; i++) {
            fiveCreatureTurnOrder[i] = null;
        }
        return fiveCreatureTurnOrder;
    }

    public void displayCreatureTurnOrder() {
        Creature[] creatureTurnOrder = getCreatureTurnOrder();
        System.out.print("TURN ORDER BANNER: ");
        for (Creature creature : creatureTurnOrder) {
            if (creature != null) {
                System.out.print(creature + ": " + creature.getTurnsAssigned() + " times speed of " + creature.getCumulativeTurnSpeed() + ", ");
            }
        }
        System.out.println();
    }

    public void displayCreatureSpeedPairsForTurnOrder() {
        CreatureSpeedTurnTriplet[] allCreatureSpeedTurnOrder = new CreatureSpeedTurnTriplet[creaturePriority.size()];
        creaturePriority.toArray(allCreatureSpeedTurnOrder);
        Arrays.sort(allCreatureSpeedTurnOrder);
        CreatureSpeedTurnTriplet[] fiveCreatureSpeedTurnOrder = new CreatureSpeedTurnTriplet[5];
        System.arraycopy(allCreatureSpeedTurnOrder, 0, fiveCreatureSpeedTurnOrder, 0, Math.min(5, creaturePriority.size()));
        for (int i = creaturePriority.size(); i < 5; i++) {
            fiveCreatureSpeedTurnOrder[i] = null;
        }
        System.out.print("TURN ORDER BANNER: ");
        for (CreatureSpeedTurnTriplet pair : fiveCreatureSpeedTurnOrder) {
            if (pair != null) {
                System.out.print(pair.toString() + ", ");
            }
        }
        System.out.println();
    }

    public Creature getCreaturePlayingTurn() {
        return creaturePlayingTurn;
    }

    // If zombie already has a target, a different target is only considered if actually closer
    public Coordinates getCoordinatesForBestClosestTarget(Zombie zombie) {
        Coordinates nextMove = null;
        if (zombieIsAdjacentToAGoodCreature(zombie)) {
            if (zombie.getStepsToCurrentTarget() > 1) {
                zombie.setCurrentTarget(getTargetAdjacentToZombie(zombie));
                zombie.setStepsToCurrentTarget(1);
                //System.out.println("*** " + zombie + " is NOW adjacent to " + getTargetAdjacentToZombie(zombie));
            } else {
                //System.out.println("*** " + zombie + " WAS AND STILL IS adjacent to " + getTargetAdjacentToZombie(zombie));
            }
        } else {
            boolean[][] availableMoves = getCalculatedOverlayForCreatureMoves(zombie);
            List<Coordinates> enemyCoordinates = getCoordinatesListOfAllEnemies(zombie);
            TreeMap<Coordinates, Creature> coordsLeadingToEnemies = generateInitialCoordinatesLeadingToEnemies(enemyCoordinates);
            List<Coordinates> desirableCoordinatesToReach;
            List<Coordinates> reachableDesirableCoordinates = new ArrayList<Coordinates>();
            int additionnalStepsToTarget = 0;
            do {
                desirableCoordinatesToReach = updateCoordinatesLeadingToEnemiesByAddingCardinalCoordinates(coordsLeadingToEnemies);
                //System.out.println("Current known coordinates: " + coordsLeadingToEnemies.keySet());
                additionnalStepsToTarget++;
                for (Coordinates coords : desirableCoordinatesToReach) {
                    int xCoord = coords.getXCoord();
                    int yCoord = coords.getYCoord();
                    if (availableMoves[xCoord][yCoord]) {
                        availableMoves[xCoord][yCoord] = false; // The move no longer needs to be considered available
                        reachableDesirableCoordinates.add(coords);
                    }
                }
            } while (reachableDesirableCoordinates.isEmpty() && overlayContainsATrueValue(availableMoves));
            System.out.println("Reachable desirable coordinates: " + reachableDesirableCoordinates);
            // Re-evalute target if closer to moves overlay than previously chosen target
            nextMove = selectSingleClosestReachableCoordinates(zombie, reachableDesirableCoordinates);
            Creature oldTarget = zombie.getCurrentTarget();
            if (oldTarget != null) {
                int bestOverlayDistanceForSameTarget = Integer.MAX_VALUE;
                for (Coordinates reachableCoords : reachableDesirableCoordinates) {
                    if (oldTarget.equals(coordsLeadingToEnemies.get(reachableCoords))) {
                        int currentOverlayDistance = paths.getCalculatedPathDistanceForCoordinates(reachableCoords);
                        if (currentOverlayDistance < bestOverlayDistanceForSameTarget) {
                            //System.out.print(reachableCoords + " is " + currentOverlayDistance + " tiles away from " + zombie + "...");
                            bestOverlayDistanceForSameTarget = currentOverlayDistance;
                        }
                    }
                }
                if (nextMove != null
                        && (bestOverlayDistanceForSameTarget == Integer.MAX_VALUE || paths.getCalculatedPathDistanceForCoordinates(nextMove) < bestOverlayDistanceForSameTarget)) {
                    Creature nextTarget = coordsLeadingToEnemies.get(nextMove);
                    zombie.setCurrentTarget(nextTarget);
                    zombie.setStepsToCurrentTarget(additionnalStepsToTarget);
                    //System.out.println("...but " + nextMove + " is closer to an enemy, being " + additionnalStepsToTarget + " steps from new target, " + nextTarget);
                }

            } else {
                Creature nextTarget = coordsLeadingToEnemies.get(nextMove);
                zombie.setCurrentTarget(nextTarget);
                zombie.setStepsToCurrentTarget(additionnalStepsToTarget);
                //System.out.println("Acquiring first target: moving to " + nextMove + " to be " + additionnalStepsToTarget + " steps from first target, " + nextTarget);
            }
            zombie.setStepsToCurrentTarget(additionnalStepsToTarget);
        }
        /*
        if (nextMove != null) {
            System.out.println("...will be moving to " + nextMove + " to pursue " + zombie.getCurrentTarget());
        }*/
        return nextMove;
    }

// Returns a list of the new coordinates added
    private List<Coordinates> updateCoordinatesLeadingToEnemiesByAddingCardinalCoordinates(TreeMap<Coordinates, Creature> coordsLeadingToEnemies) {
        Set<Coordinates> previouslyKnownCoordsSet = coordsLeadingToEnemies.keySet();
        List<Coordinates> nextValidCoordsList = new ArrayList<Coordinates>();
        TreeMap<Coordinates, Creature> additionalCoordsForCreatures = new TreeMap<Coordinates, Creature>();
        for (Coordinates rootCoords : previouslyKnownCoordsSet) {
            Creature rootEnemy = coordsLeadingToEnemies.get(rootCoords);
            for (Coordinates cardinalCoords : rootCoords.getFourSurroundingCardinalCoordinates()) {
                //System.out.println("NEW CANDIDATE COORDINATED: " + cardinalCoords);
                if (gameboard.containsTileWithCoordinates(cardinalCoords)
                        && !previouslyKnownCoordsSet.contains(cardinalCoords) && !nextValidCoordsList.contains(cardinalCoords)) {
                    //System.out.println("              NEW COORDINATED ADDED: " + cardinalCoords);
                    nextValidCoordsList.add(cardinalCoords); // Take note of new valid cardinal coords
                    additionalCoordsForCreatures.put(cardinalCoords, rootEnemy); // Take note of root enemy related to new cardinal coords
                }
            }
        }
        //System.out.println("BEFORE: " + coordsLeadingToEnemies);
        coordsLeadingToEnemies.putAll(additionalCoordsForCreatures);
        //System.out.println("AFTER: " + coordsLeadingToEnemies);
        return nextValidCoordsList;
    }

    private TreeMap<Coordinates, Creature> generateInitialCoordinatesLeadingToEnemies(List<Coordinates> enemyCoordinates) {
        TreeMap<Coordinates, Creature> coordsLeadingToEnemies = new TreeMap<Coordinates, Creature>();
        for (Coordinates coords : enemyCoordinates) {
            Creature enemy = gameboard.getCreatureAt(coords);
            coordsLeadingToEnemies.put(coords, enemy);
        }
        return coordsLeadingToEnemies;
    }

    private boolean overlayContainsATrueValue(boolean[][] overlay) {
        boolean foundATrueValue = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (overlay[i][j]) {
                    foundATrueValue = true;
                    break;
                }
            }
        }
        return foundATrueValue;
    }

    public List<Coordinates> getCoordinatesListOfAllEnemies(Creature creature) {
        boolean[][] overlay = gameboard.generateBooleanOverlayWithAllValuesSetTo(true);
        return getCoordinatesListOfEnemiesWithinOverlayOfCreature(creature, overlay);
    }

    public List<Coordinates> getCoordinatesListOfEnemiesWithinOverlayOfCreature(Creature creature, boolean[][] overlay) {
        List<Coordinates> coordsList = new ArrayList<Coordinates>();
        boolean enemyAlignment = !creature.isGood();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Creature creatureOnTile = gameboard.getTileAt(i, j).getOccupier();
                if (overlay[i][j] && creatureOnTile != null && (creatureOnTile.isGood() == enemyAlignment)) {
                    coordsList.add(new Coordinates(i, j));
                }
            }
        }
        return coordsList;
    }

    private List<Coordinates> getAllGameBoardCoordinatesAdjacentTo(List<Coordinates> targetCoords) {
        List<Coordinates> validAdjacentCoords = new ArrayList<Coordinates>();
        for (Coordinates coords : targetCoords) {
            Coordinates[] fourAdjacentCoords = coords.getFourSurroundingCardinalCoordinates();
            for (Coordinates adjacentCoords : fourAdjacentCoords) {
                if (gameboard.containsTileWithCoordinates(adjacentCoords)) {
                    validAdjacentCoords.add(adjacentCoords);
                }
            }
        }
        return validAdjacentCoords;
    }

    private Coordinates selectSingleClosestReachableCoordinates(Creature creature, List<Coordinates> adjacentCoords) {
        int minDistance = Integer.MAX_VALUE;
        Coordinates closestCoords = null;
        for (Coordinates coords : adjacentCoords) {
            int calculatedDistanceToCoords = paths.getCalculatedPathDistanceForCoordinates(coords);
            if (calculatedDistanceToCoords < minDistance) {
                minDistance = calculatedDistanceToCoords;
                closestCoords = coords;
            }
        }
        return closestCoords;
    }

    private MotionEvent moveCreatureRandomly(Creature creature) {
        if (creature.isAlignedTo("bad")) {

            creature.setEnergy(12); // Make sure enemy creature can move somewhat each turn
        }
        System.out.println("Moving creature randomly: " + creaturePlayingTurn);
        drawWithOverlayForCreatureMoves(creature);
        int validMoves = 0;
        // Counting valid moves (first pass)
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (currentOverlay[i][j]) {
                    validMoves++;
                }
            }
        }
        Random generator = new Random();
        int randomMove = generator.nextInt(validMoves);
        int movesCounter = 0;
        MotionEvent motionEvent = null;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (currentOverlay[i][j] && movesCounter++ == randomMove) {
                    motionEvent = moveCreatureTo(creature, new Coordinates(i, j));
                    break;
                }
            }
        }
        return motionEvent;
    }

    private Coordinates getCoordsOfFirstGoodCreatureAdjacentToZombie(Creature zombie) {
        Coordinates zombieCoords = gameboard.getCreatureCoordinates(zombie);
        Coordinates enemyCoords = null;
        Coordinates[] cardinalCoordinates = zombieCoords.getFourSurroundingCardinalCoordinates();
        for (Coordinates coords : cardinalCoordinates) {
            if (gameboard.containsTileWithCoordinates(coords) && gameboard.tileIsOccupied(coords)) {
                Tile targetTile = gameboard.getTileAt(coords);
                Creature targetCreature = targetTile.getOccupier();
                if (targetCreature.isGood() && targetCreature.isAlive()) {
                    enemyCoords = coords;
                    break;
                }
            }
        }
        return enemyCoords;
    }

    public Creature getTargetAdjacentToZombie(Creature zombie) {
        Creature target = null;
        Coordinates targetCoords = getCoordsOfFirstGoodCreatureAdjacentToZombie(zombie);
        if (targetCoords != null) {
            target = gameboard.getCreatureAt(targetCoords);
        }
        return target;
    }

    public MotionEvent haveZombieAttackAnyAdjacentGoodCreatures(Creature zombie) {
        int zombieSkill = 1;
        Coordinates targetCoords = getCoordsOfFirstGoodCreatureAdjacentToZombie(zombie);
        MotionEvent attackEvent;
        if (targetCoords != null) {
            attackEvent = useCreatureSkillAt(zombie, zombieSkill, targetCoords);
        } else {
            attackEvent = null;
        }
        return attackEvent;
    }

    public void activateGameOver() {
        gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    boolean zombieIsAdjacentToAGoodCreature(Creature zombie) {
        return getCoordsOfFirstGoodCreatureAdjacentToZombie(zombie) != null;
    }
}
