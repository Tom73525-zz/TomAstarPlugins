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
        
        if(curNode.getParent()==null){
            
            return heuristic;
        }
            
        Node adjacentNode = new Node(adjNode);
        Node currentNode = new Node(curNode);
        Node cur_parentNode = new Node(curNode.getParent());
         
        // Zag condition: P' Zags when A(i-2).col - A(i-1).col != A(i-1).col - A(i).col 
        //                          or A(i-2).row - A(i-1).row != A(i-1).col - Ai(i).row
        
        boolean isZagging = ( cur_parentNode.getCol() - currentNode.getCol() ) 
                             != ( currentNode.getCol() - adjacentNode.getCol())
                                || ( cur_parentNode.getRow() - currentNode.getRow() ) 
                                    != ( currentNode.getRow() - adjacentNode.getRow() );
        
        boolean isTracking = (tracksWall( tileMap, adjNode ) || tracksWall( tileMap, curNode ) );
        
        //Experimentally determined value of tau(Inertia streak) is 8
        
        if(adjNode.getInertia()>=8){
            
            return heuristic;  // if inertia reaches 8, it is reset
        }
        else if( !isZagging && !isTracking ){
            adjNode.setInertia(curNode.getInertia()+1);    // if the agent does not zag, inertia is incremented by 1. 
            return heuristic;
        }
        else if( isZagging && !isTracking ){
            return heuristic+2;                            // penalty is also imposed on the agent whenever it zags.  
        }
        else if( !isZagging && isTracking){
            adjNode.setInertia(curNode.getInertia()+1);
            return heuristic+1000;                           // penalty is imposed on the agent whenever it tracks a wall/obstacle
        }
        else{
            return heuristic+1300;
        }
    }

    @Override
    public void complete(Node curNode) {
        
        
        
        while(curNode.getParent().getParent() != null)
        {
        
            System.out.println("go");
            boolean isZagging = ( curNode.getParent().getParent().getCol() - curNode.getParent().getCol() ) 
                             != ( curNode.getParent().getCol() - curNode.getCol())
                                || ( curNode.getParent().getParent().getRow() - curNode.getParent().getRow() ) 
                                    != ( curNode.getParent().getRow() - curNode.getRow() );
            if( isZagging ){
                System.out.println("Zagged");
                shape(curNode.getCost(),curNode.getParent(),curNode);                        // penalty is also imposed on the agent whenever it zags.  
                
                
            }
            curNode=curNode.getParent();
        }
        
    }
    
}
