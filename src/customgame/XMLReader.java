/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class XMLReader 
{
    private String filePath;
    private BufferedReader bufferedReader;
    
    public XMLReader(String path)
    {
        filePath = path;
    }
    
    public String getElement(String header)
    {
        String element = "";
        boolean complete = false;
        boolean found = false;
        boolean headerComplete = false;
        try 
        {
            FileReader fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null &! complete)
            {
                if(line.contains("<"+ header) || found)
                {
                    if(!found)
                        found = true;
                    
                    element+=line;
                    
                    if(!headerComplete && line.contains(">") &! line.contains("/>"))
                    {
                        headerComplete = true;
                    }
                    if(headerComplete && line.contains("</" + header + ">"))
                    {
                        complete = true;
                    }
                    else if(!headerComplete && line.contains("/>"))
                    {
                        complete = true;
                    }
                }
            }
        } 
        catch (IOException ex) 
        {
            System.out.println("XMLReader: getElement(" + header + "): error");
        }
        return element;
    }
    
    public ArrayList<String> getElements(String header)
    {
        ArrayList<String> elements = new ArrayList();
        boolean complete = false;
        boolean found = false;
        boolean headerComplete = false;
        try 
        {
            FileReader fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            String element = "";
            while((line = bufferedReader.readLine()) != null)
            {
                if(line.contains("<"+ header) || found)
                {
                    if(!found)
                        found = true;
                    
                    element+=line;
                    
                    if(!headerComplete && line.contains(">") &! line.contains("/>"))
                    {
                        headerComplete = true;
                    }
                    if(headerComplete && line.contains("</" + header + ">"))
                    {
                        elements.add(element);
                        element = "";
                        headerComplete = false;
                        found = false;
                    }
                    else if(!headerComplete && line.contains("/>"))
                    {
                        elements.add(element);
                        element = "";
                        headerComplete = false;
                        found = false;
                    }
                }
            }
        } 
        catch(FileNotFoundException f)
        {
            System.out.println("XMLReader: getElements(" + header + "): could not find file");
            
        }
        catch (IOException ex) 
        {
            System.out.println("XMLReader: getElements(" + header + "): error");
        }
        
        return elements;
    }
    
    public static ArrayList<String> getSubElements(String element, String header)
    {
        ArrayList<String> subElements = new ArrayList();
        String subElement = "";
        int currentIndex = 0;
        //System.out.println("XMLReader: getSubElements(element = " + element + ", header = " + header + ")");
        //int counter = 0;
        while(element.substring(currentIndex).contains("<" + header + " "))
        {
            //counter++;
            //System.out.println("Loop #" + counter);
            //System.out.println("element.substring(currentIndex)=[[" + element.substring(currentIndex) + "]]");
            int index1 = element.substring(currentIndex).indexOf("<" + header + " ");
            int index2 = index1;
            
            if(element.substring(currentIndex).substring(index1).contains("</" + header + ">"))
            {
                index2 = element.substring(currentIndex).substring(index1).indexOf("</" + header + ">") + (header.length() + 3) + index1;
            }
            else if(element.substring(currentIndex).substring(index1).contains("/>"))
            {
                index2 = element.substring(currentIndex).substring(index1).indexOf("/>") + 2 + index1;
            }
            else
            {
                System.out.println("XMLReader: getSubElements(element, " + header + ") could not find end sign!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
            //System.out.println("index1 = " + index1 + ", index2=" + index2 + ", currentIndex=" + currentIndex);
            subElement = element.substring(currentIndex).substring(index1, index2);
            currentIndex += index2;
            subElements.add(subElement);
        }
        
        return subElements;
    }
    
    public static String getSubElement(String element, String header)
    {
        //System.out.println("XMLReader: getSubElement(" + element + ", " + header + ")");
        String subElement = "";
        int index1 = 0;
        if(element.contains("<" + header + " "))
            index1 = element.indexOf("<" + header + " ");
        else if(element.contains("<" + header + ">"))
            index1 = element.indexOf("<" + header + ">");
        else
            System.out.println("XMLReader: getSubElement: could not find header");
            
        int index2 = index1;
        if(element.contains("</" + header + ">"))
        {
            index2 = element.indexOf("</" + header + ">") + (header.length() + 3);
        }
        else if(element.contains("/>"))
        {
            index2 = index1 + element.substring(index1).indexOf("/>") + 2;
        }
        else
        {
            System.out.println("XMLReader: getSubElement(element, " + header + ") could not find end sign");
        }
        //System.out.println("index1 = " + index1 + ", index2 = " + index2);
        subElement = element.substring(index1, index2);
        
        return subElement;
//        String subElement = "";
//        if(element.contains(header))
//        {
//            int index1 = element.indexOf("<" + header);
//            int index2 = element.indexOf("</" + header+ ">");
//            int index3 = element.indexOf("/>") + 2;
//            int index4;
//            if(index2 < 0 && index3 > 0)
//                index4 = index3;
//            else if(index2 > 0 && index3 < 0)
//                index4 = index2;
//            else if(index2 > 0 && index3 > 0)
//                index4 = Math.min(index2, index3);
//            else
//                index4 = index1;
//            System.out.println("index1 = " + index1 + ", index4 = " + index4);
//            subElement = element.substring(index1, index4);
//        }
//        else
//        {
//            System.out.println("XMLReader: getSubElement: element " + element + " does not contain " + header);
//        }
//        return subElement;
    }
    
    public static String getAttribute(String element, String name)
    {
        String attribute = "";
        
        boolean complete = false;
        if(element.contains(name + "="))
        {
            int index1 = element.indexOf(name + "=") + name.length()+2;
            int index2 = element.indexOf("\"", index1);
            attribute = element.substring(index1, index2);
            complete = true;
        }
        
        return attribute;
    }
}
