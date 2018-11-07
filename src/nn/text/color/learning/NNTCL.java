 
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nn.text.color.learning;

import customgame.Game;
import customgame.states.StateMachine;
import java.text.DecimalFormat;
import neuralnetworks.Network;


public class NNTCL 
{
    private Game game;
    private StateMachine stateMachine;
    public final static int WIDTH = 900, HEIGHT = 900;
    public final static DecimalFormat DF = new DecimalFormat("#.##");
    private Network winner;
    
    public NNTCL()
    {
        game = new Game("TEXT COLOR LEARNING PROGRAM", WIDTH, HEIGHT);
        stateMachine = game.getStateMachine();
        stateMachine.add("base", new BaseState(game, this));
        stateMachine.add("display", new DisplayState(game, this));
        stateMachine.change("base");
        game.run();
    }
    
    public void setWinner(Network network)
    {
        winner = network;
    }
    
    public Network getWinner()
    {
        return winner;
    }
    
    public static void main(String[] args) 
    {
       NNTCL nntcl = new NNTCL();
    }

}
