/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nn.text.color.learning;

import customgame.Game;
import customgame.customui.Button;
import customgame.states.IState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

public class DisplayState implements IState
{
    private NNTCL nntcl;
    private Game game;
    private Button button;
    private int[] backgroundColor;
    private Color textColor;
    private Random random = new Random();
    private final long BUTTON_COOLDOWN = 100;
    private long lastButtonPress;
    
    public DisplayState(Game game, NNTCL nntcl)
    {
        this.game = game;
        this.nntcl = nntcl;
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering DisplayState");
        button = new Button(game.getGui(), NNTCL.WIDTH/2 - 100, 3*NNTCL.HEIGHT/4 - 25, 200, 50, "Next Color");
        backgroundColor = new int[3];
        for(int i = 0; i < backgroundColor.length; i++)
        {
            backgroundColor[i] = 0;
        }
        textColor = Color.WHITE;
        lastButtonPress = System.currentTimeMillis();
    }
    
    private void createColors()
    {
        for(int i = 0; i < backgroundColor.length; i++)
        {
            backgroundColor[i] = random.nextInt(256);
        }
        float[] inputs = new float[3];
        for(int i = 0; i < inputs.length; i++)
        {
            inputs[i] = (float) (backgroundColor[i]/255.0);
        }
        float[] outputs = nntcl.getWinner().getOutputs(inputs);
        for(int i = 0; i < inputs.length; i++)
        {
            System.out.println("inputs[" + i + "]="+inputs[i]);
        }
        for(int i = 0; i < outputs.length; i++)
        {
            System.out.println("outputs[" + i + "]="+outputs[i]);
        }
        if(outputs[0] >= 0.8 && outputs[1] <= 0.2)
        {
            textColor = Color.BLACK;
        }
        else if(outputs[1] >= 0.8 && outputs[0] <= 0.2)
        {
            textColor = Color.WHITE;
        }
        else
        {
            textColor = Color.RED;
        }
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting DisplayState");
    }

    @Override
    public void update() 
    {
        button.update();
        if(System.currentTimeMillis() - lastButtonPress > BUTTON_COOLDOWN)
        {
            if(button.getPressed())
            {
                lastButtonPress = System.currentTimeMillis();
                button.setSelected(false);
                createColors();
            }
        }
    }

    @Override
    public void render(Graphics g) 
    {
        g.setColor(new Color(backgroundColor[0], backgroundColor[1], backgroundColor[2]));
        g.fillRect(0, 0, NNTCL.WIDTH, NNTCL.HEIGHT);
        g.setColor(textColor);
        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
        String text = "Problemo.";
        if(textColor != Color.RED)
            text = "No problemo.";
        g.drawString(text, NNTCL.WIDTH/2 - g.getFont().getSize()*text.length()/3, NNTCL.HEIGHT/2 - g.getFont().getSize()/2);
        button.render(g);
    }

}
