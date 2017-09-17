/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astar.model;

import astar.plugin.IModel;
import static astar.util.Helper.tracksWall;
import astar.util.Node;

/**
 *
 * @author Tom
 */
public class Pretty implements IModel {
    
    protected char[][] tileMap = null;

    @Override
    public void init(char[][] tileMap) {
        this.tileMap = tileMap; 
    }

    @Override
    public double shape(double heuristic, Node curNode, Node adjNode) {
        System.out.println(curNode.getSteps());
        
        if(curNode.getSteps()<=2)
            return heuristic;    
        
        Node ai = new Node(adjNode);
        Node ai_1 = new Node(curNode);
        Node ai_2 = new Node(curNode.getParent());
        
        if(adjNode.getInertia()==8){
            adjNode.setInertia(0);
        }
        
        if(( ai_2.getCol() - ai_1.getCol() ) != ( ai_1.getCol() - ai.getCol()) 
                || (ai_2.getRow() - ai_1.getRow()) != ( ai_1.getRow() - ai.getRow() ) 
                && ( ( tracksWall( tileMap , curNode ) && tracksWall( tileMap, adjNode ) ) ) ){
            
            heuristic+=600;
            
        }
        else
        {
            adjNode.setInertia(curNode.getInertia()+1);
        }
        
        if( tracksWall( tileMap , adjNode )
            ||  tracksWall( tileMap , curNode ) ) {
            heuristic+=10000;
        }  
        
       
        if(( ai_2.getCol() - ai_1.getCol() ) != ( ai_1.getCol() - ai.getCol()) 
                || (ai_2.getRow() - ai_1.getRow()) != ( ai_1.getRow() - ai.getRow() ))
        {
            heuristic+=2;
            
        }
         else
        {
            adjNode.setInertia(curNode.getInertia()+1);
        }
        
        return heuristic;
    }

    @Override
    public void complete(Node curNode) {
        
    }
    
}
