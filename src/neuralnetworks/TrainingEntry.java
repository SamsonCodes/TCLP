/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neuralnetworks;

public class TrainingEntry 
{
    public float[] inputs;
    public float[] desiredOutputs;
    
    public TrainingEntry(float[] inputs, float[] desiredOutputs)
    {
        this.inputs = inputs;
        this.desiredOutputs = desiredOutputs;
    }

}
