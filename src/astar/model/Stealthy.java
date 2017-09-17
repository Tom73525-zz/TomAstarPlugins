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
public class Stealthy implements IModel{

    protected char[][] tileMap; 
    @Override
    public void init(char[][] tileMap) {
       this.tileMap = tileMap;
    }

    @Override
    public double shape(double heuristic, Node curNode, Node adjNode) {
        
        
     /*    if(curNode.getSteps()<=2){
            
            return heuristic;    
        }
        
        
        Node adjacentNode = new Node(adjNode);
        Node currentNode = new Node(curNode);
        Node cur_parentNode = new Node(curNode.getParent());
        
        if(adjNode.getInertia()==8){
            
            adjNode.setInertia(0);
        }
        
        if(( cur_parentNode.getCol() - currentNode.getCol() ) != ( currentNode.getCol() - adjacentNode.getCol()) 
                || ( cur_parentNode.getRow() - currentNode.getRow() ) != ( currentNode.getRow() - adjacentNode.getRow() ) 
                && ( ( tracksWall( tileMap , curNode ) && tracksWall( tileMap, adjNode ) ) ) ){
            
            heuristic+=1000;
            
        }
        else{
            
            adjNode.setInertia(curNode.getInertia()+1);
            
        }
        */
        if( tracksWall( tileMap , adjNode )
            ||  tracksWall( tileMap , curNode ) ) {
            
            heuristic-=heuristic*0.15;
        }  
        /*s
       
        if(( cur_parentNode.getCol() - currentNode.getCol() ) != ( currentNode.getCol() - adjacentNode.getCol()) 
                || (cur_parentNode.getRow() - currentNode.getRow()) != ( currentNode.getRow() - adjacentNode.getRow() )){
            heuristic+=5000;
        }
        else{
            adjNode.setInertia(curNode.getInertia()+1);
        }
        
        */
        return heuristic;
    }

    @Override
    public void complete(Node curNode) {
        
    }
    
}
