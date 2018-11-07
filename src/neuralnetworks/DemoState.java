/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neuralnetworks;

import customgame.states.IState;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class DemoState implements IState
{
    int inputNr = 3;
    float[] inputs = new float[inputNr];
    Network network = new Network(inputNr, new int[]{7, 3}, 2, false, false);
    Network load;
    long lastInput;
    private final long INPUT_INTERVAL = 1000;
    private Random random = new Random();
    private SaveManager saveManager = new SaveManager();
    
    private void createInputs()
    {
        for(int i = 0; i < inputNr; i++)
        {
            inputs[i] = (random.nextFloat()*2 - 1);
        }
    }
    
    @Override
    public void onEnter() 
    {
        System.out.println("Entering DemoState");
        createInputs();
        lastInput = System.currentTimeMillis();
        ArrayList<String> loadData = saveManager.loadData("network");
        if(loadData != null)
        {
            load = new Network(loadData.get(0));
        }
        String data = network.createSaveData();
        ArrayList<String> saveData = new ArrayList();
        saveData.add(data);
        saveManager.saveData(saveData, "network");
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting DemoState");
    }

    @Override
    public void update() 
    {
        if(System.currentTimeMillis() - lastInput > INPUT_INTERVAL)
        {
            lastInput = System.currentTimeMillis();
            createInputs();
        }
    }

    @Override
    public void render(Graphics g) 
    {
        network.render(g, 0, 0, 600, 900, inputs);
        if(load != null)
            load.render(g, 600, 0, 600, 900, inputs);
    }

}
