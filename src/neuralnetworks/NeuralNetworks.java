/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neuralnetworks;

import customgame.Game;


public class NeuralNetworks 
{
    
    public NeuralNetworks()
    {
        Game game = new Game("NeuralNetworks", 1200, 900);
        game.getStateMachine().add("demo", new DemoState());
        game.getStateMachine().change("demo");
        game.run();
    }

    public static void main(String[] args) 
    {
        NeuralNetworks neuralNetworks = new NeuralNetworks();
    }

}
