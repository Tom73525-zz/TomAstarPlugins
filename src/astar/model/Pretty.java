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
        
        this.tileMap = tileMap;    // Store world tileMap in class memeber variable.                       
    }

    @Override
    public double shape(double heuristic, Node curNode, Node adjNode) {
        
        if(curNode.getSteps()<=2){
            
            return heuristic;    
        }
        
        Node adjacentNode = new Node(adjNode);
        Node currentNode = new Node(curNode);
        Node cur_parentNode = new Node(curNode.getParent());
        
        //Experimentally determined value of tau(Inertia streak) is 8
        if(adjNode.getInertia()==8){
            
            adjNode.setInertia(0);  // if inertia reaches 8, it is reset
        }
        
        // Zag condition: P' Zags when A(i-2).col - A(i-1).col != A(i-1).col - A(i).col 
        //                          or A(i-2).row - A(i-1).row != A(i-1).col - Ai(i).row
        
        if(( cur_parentNode.getCol() - currentNode.getCol() ) != ( currentNode.getCol() - adjacentNode.getCol()) 
                || ( cur_parentNode.getRow() - currentNode.getRow() ) != ( currentNode.getRow() - adjacentNode.getRow() ) 
                && ( ( tracksWall( tileMap , curNode ) && tracksWall( tileMap, adjNode ) ) ) ){
            
            heuristic+=600;
            
        }
        else{
            
            adjNode.setInertia(curNode.getInertia()+1);     // if the agent does not zag, inertia is incremented by 1. 
            
        }
        
        if( tracksWall( tileMap , adjNode )
            ||  tracksWall( tileMap , curNode ) ) {
            
            heuristic+=10000;                              // penalty is imposed on the agent whenever it tracks a wall/obstacle
        }  
        
        if(( cur_parentNode.getCol() - currentNode.getCol() ) != ( currentNode.getCol() - adjacentNode.getCol()) 
                || (cur_parentNode.getRow() - currentNode.getRow()) != ( currentNode.getRow() - adjacentNode.getRow() )){
            
            heuristic+=2;                                  // penalty is also imposed on the agent whenever it zags.  
        }
        else{
            
            adjNode.setInertia(curNode.getInertia()+1);
        }
        
        return heuristic;
    }

    @Override
    public void complete(Node curNode) {
        
    }
    
}
