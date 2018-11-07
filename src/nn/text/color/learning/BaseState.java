 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nn.text.color.learning;

import customgame.Game;
import customgame.states.IState;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import neuralnetworks.Network;
import neuralnetworks.TrainingEntry;
//!!!!!!!!!!!!Colors are inverted from the RGB coding: i thought high RGB values meant dark color and low values light
//but it turned out to be the other way around;
public class BaseState implements IState
{
    private Game game;
    private NNTCL nntcl;
    private Network network;
    private float[] inputs = {0.13f,0.26f,0.13f}; //white
    private float[][] testInputs = {{0.10f,0.20f,0.15f},{0.99f,0.75f,0.75f},{0.5f,0.78f,0.88f}};
    private ArrayList<TrainingEntry> trainingData;
    int attempts;
    private Random random = new Random();
    private final static int HLSIZE = 3, OLSIZE = 2;
    private final boolean HP = false, OP = false;
    
    
    public BaseState(Game game, NNTCL nntcl)
    {
        this.game = game;
        this.nntcl = nntcl;
    }
    @Override
    public void onEnter() 
    {
        System.out.println("Entering Base State");
        trainingData = new ArrayList();
        //trainingData.add(new TrainingEntry(new double[]{0, 0, 0}, new double[]{0, 1})); //white = black
        trainingData.add(new TrainingEntry(new float[]{0.01f, 0.01f, 0.01f}, new float[]{0, 1}));//white = black
//        trainingData.add(new TrainingEntry(new float[]{0.49, 0.49, 0.49}, new float[]{0, 1}));//mid
//        trainingData.add(new TrainingEntry(new float[]{0.51, 0.51, 0.51}, new float[]{1, 0}));//mid
        trainingData.add(new TrainingEntry(new float[]{0.99f, 0.99f, 0.99f}, new float[]{1, 0}));//black = white
        //trainingData.add(new TrainingEntry(new float[]{1, 1, 1}, new float[]{1, 0}));//black = white
        trainingData.add(new TrainingEntry(new float[]{0.99f, 0.01f, 0.01f}, new float[]{0, 1}));//red = black
        trainingData.add(new TrainingEntry(new float[]{0.01f, 0.99f, 0.01f}, new float[]{0, 1}));//green = black
        trainingData.add(new TrainingEntry(new float[]{0.01f, 0.01f, 0.99f}, new float[]{0, 1}));//blue =  black
        trainingData.add(new TrainingEntry(new float[]{0.01f, 0.99f, 0.99f}, new float[]{1, 0}));//green blue = white
        trainingData.add(new TrainingEntry(new float[]{0.99f, 0.01f, 0.99f}, new float[]{1, 0}));//red blue = white
        trainingData.add(new TrainingEntry(new float[]{0.99f, 0.99f, 0.01f}, new float[]{1, 0}));//red green = white
        
        trainingData.add(new TrainingEntry(new float[]{0.75f, 0.25f, 0.25f}, new float[]{0, 1}));//red = black
        trainingData.add(new TrainingEntry(new float[]{0.25f, 0.75f, 0.25f}, new float[]{0, 1}));//green = black
        trainingData.add(new TrainingEntry(new float[]{0.25f, 0.25f, 0.75f}, new float[]{0, 1}));//blue =  black
        trainingData.add(new TrainingEntry(new float[]{0.25f, 0.75f, 0.75f}, new float[]{1, 0}));//green blue = white
        trainingData.add(new TrainingEntry(new float[]{0.75f, 0.25f, 0.75f}, new float[]{1, 0}));//red blue = white
        trainingData.add(new TrainingEntry(new float[]{0.75f, 0.75f, 0.25f}, new float[]{1, 0}));//red green = white
        trainingData.add(new TrainingEntry(new float[]{0.75f, 0.25f, 0.25f}, new float[]{0, 1}));//red = black
        trainingData.add(new TrainingEntry(new float[]{0.25f, 0.75f, 0.25f}, new float[]{0, 1}));//green = black
        trainingData.add(new TrainingEntry(new float[]{0.25f, 0.25f, 0.75f}, new float[]{0, 1}));//blue =  black
        trainingData.add(new TrainingEntry(new float[]{0.25f, 0.75f, 0.75f}, new float[]{1, 0}));//green blue = white
        trainingData.add(new TrainingEntry(new float[]{0.75f, 0.25f, 0.75f}, new float[]{1, 0}));//red blue = white
        trainingData.add(new TrainingEntry(new float[]{0.75f, 0.75f, 0.25f}, new float[]{1, 0}));//red green = white
        
        network = evolve();
        
        //System.out.println("Attempts = " + attempts);
//        System.out.print("Input = ");
//        for(double d: inputs)
//        {
//            System.out.print(d + ", ");
//        }
//        System.out.println();
//        double[] outputs = network.getOutputs(inputs);
//        for(int i = 0; i < outputs.length; i++)
//        {
//            System.out.println("Output" + i + " = " + NNTCL.DF.format(outputs[i]));
//        }
        
        System.out.println("Cost = " + NNTCL.DF.format(getCost(network)));
        System.out.println();
        
//        for(float[] input: testInputs)
//        {
//            System.out.print("Input = ");
//            for(float d: input)
//            {
//                System.out.print(d + ", ");
//            }
//            System.out.println();
//            float[] outputs = network.getOutputs(input);
//            for(int i = 0; i < outputs.length; i++)
//            {
//                System.out.println("Output" + i + " = " + NNTCL.DF.format(outputs[i]));
//            }
//            System.out.println();
//        }
        
        nntcl.setWinner(network);
    }
    
    public Network randomPick()
    {
        attempts = 1;
        int maxAttempts = 1000000;
        Network randomNetwork = new Network(inputs.length, new int[]{HLSIZE}, OLSIZE, HP, OP);
        double cost = getCost(randomNetwork);
        double maxCost = cost;
        Network bestNetwork = randomNetwork;
        while(cost > 0.1 && attempts < maxAttempts)
        {
            randomNetwork = new Network(inputs.length, new int[]{HLSIZE}, OLSIZE, HP, OP);
            cost = getCost(randomNetwork);
            if(cost < maxCost)
            {
                System.out.println("cost = " + cost);
                maxCost = cost;
                bestNetwork = randomNetwork;
            }
            attempts++;
        }
        System.out.println();
        return bestNetwork;
    }
    
//    public Network gradientDescent()
//    {
//        Network randomNetwork = new Network(inputs.length, new int[]{HLSIZE}, OLSIZE, HP, OP);
//        double[][] outputs = new double[trainingData.size()][OLSIZE];
//        for(int i = 0; i < outputs.length; i++)
//        {
//            for(int j = 0; j < outputs[0].length; j++)
//            {
//                outputs[i][j] = randomNetwork.getOutputs(trainingData.get(i).inputs)[j];
//            }
//        }
//        double[][] desiredOutputs = new double[trainingData.size()][OLSIZE];
//        for(int i = 0; i < outputs.length; i++)
//        {
//            for(int j = 0; j < outputs[0].length; j++)
//            {
//                desiredOutputs[i][j] = trainingData.get(i).desiredOutputs[j];
//            }
//        }
//        int W = inputs.length * HLSIZE + HLSIZE * OLSIZE;
//        int B = HLSIZE + OLSIZE;
//        double r = 0.001;
//        double delta = 0.0001;
//        double[] gradient = new double[W + B];
//        for(int i = 0; i < HLSIZE; i++)
//        {
//            for(int j = 0; j < inputs.length; i++)
//            {
//                double[] deltaOutput = new double[OLSIZE];
//                //deltaOutput[0] = 
//                //gradient[i * inputs.length + j] =  
//            }
//        }
//    }
    
    public Network evolve()
    {
        System.out.println("Evolving network");
        int popSize = 1000;
        int generations = 1000;
        int genId = 1;
        Network[] currentGen = new Network[popSize];
        for(int i = 0; i < popSize; i++)
        {
            currentGen[i] = new Network(inputs.length, new int[]{HLSIZE}, OLSIZE, HP, OP);
        }
        String s = "";
        boolean stagnant = false;
        double[] history = new double[100];
        while(genId <= generations &!stagnant)
        {
            s += "Gen" + genId + "\n";
            //sort from low to high cost;
            for(int i = 0; i < (popSize - 1); i++)
            {
                int j = i;
                boolean nextIndex = false;
                while(j >= 0 &! nextIndex)
                { 
                    if (getCost(currentGen[j+1]) < getCost(currentGen[j]))
                    {
                        Network holder = currentGen[j + 1];
                        currentGen[j] = currentGen[j+1];
                        currentGen[j + 1] = holder;
                        j--;
                    }
                    else
                    {
                        nextIndex = true;
                    }
                }
            }
            for(int i = (history.length - 1); i >= 1; i--)
            {
                history[i] = history[i-1];
            }
            history[0] = getCost(currentGen[0]);
            if(genId >= history.length)
            {
                if(Math.abs(history[0] - history[history.length-1]) < 0.01)
                {
                    System.out.println("history[0] = " + history[0]);
                    System.out.println("history[" + (history.length - 1) + "] = " + history[history.length - 1]);
                    System.out.println();
                    System.out.println("Complete History:");
                    for(int i = 0; i < history.length; i++)
                    {
                        System.out.println("history[" + i + "] = " + history[i]);
                    }
                    System.out.println();
                    stagnant = true;    //breaks the loop if the cost has not changed significantly over (history.length) generations                    
                }
            }
                //System.out.println("Network"+i + " cost = " + NNTCL.DF.format(getCost(currentGen[i])));
            s+="Network"+0 + " cost = " + NNTCL.DF.format(getCost(currentGen[0])) + "\n";
            s+="Network"+(popSize/2-1) + " cost = " + NNTCL.DF.format(getCost(currentGen[popSize/2-1])) + "\n";
            s+="Network"+(popSize-1) + " cost = " + NNTCL.DF.format(getCost(currentGen[popSize-1])) + "\n";
            //System.out.println("");
            //save the best half
            Network[] survivors = new Network[popSize/2];
            for(int i = 0; i < popSize/2; i++)
            {
                survivors[i] = currentGen[i];
            }
            //create offspring
            float mutation = 0.05f; 
            for(int i = 0; i < popSize; i++)
            {
                if(i < popSize/2)
                {
                    currentGen[i] = new Network(currentGen[i], mutation);
                }
                else
                {
                    currentGen[i] = new Network(inputs.length, new int[]{HLSIZE}, OLSIZE, HP, OP);
                }
            }
            genId++;
        }
        Network bestNetwork = currentGen[0];
        for(int i = 0; i < popSize; i++)
        {
            if(getCost(currentGen[i]) < getCost(bestNetwork))
            {
                bestNetwork = currentGen[i];
            }
        }
        System.out.println(s);
        return bestNetwork;
    }
    
    public double getCost(Network network)
    {
        double cost = 0;
        for(TrainingEntry t: trainingData)
        {
            float[] outputs = network.getOutputs(t.inputs);
            for(int i = 0; i < outputs.length; i++)
            {
                cost += Math.pow(Math.abs(outputs[i] - t.desiredOutputs[i]), 2);
            }
        }
        //System.out.println("Cost = " + cost);
        return cost/(2*trainingData.size());
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting Base State");
    }

    @Override
    public void update() 
    {
        if(game.getGui().getKeyManager().keys[KeyEvent.VK_SPACE])
        {
            game.getStateMachine().change("display");
        }
    }

    @Override
    public void render(Graphics g) 
    {
        network.render(g, 0, 0, NNTCL.WIDTH, NNTCL.HEIGHT);
    }

}
