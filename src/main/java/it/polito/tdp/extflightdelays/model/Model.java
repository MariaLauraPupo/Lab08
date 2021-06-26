package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private SimpleWeightedGraph<Airport,DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao;
	private Map<Integer,Airport> idMap;
	
	public Model() {
		dao = new ExtFlightDelaysDAO();
		idMap= new HashMap<Integer,Airport>();
		dao.loadAllAirports(idMap);
	}
	
	public void creaGrafo(double distanza) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungo i vertici
		Graphs.addAllVertices(grafo, idMap.values());
		//aggiungo gli archi
		for(Rotta r : dao.getRotte(distanza, idMap)) {
			if(this.grafo.containsVertex(r.getPartenza()) && this.grafo.containsVertex(r.getDestinazione())) {
				DefaultWeightedEdge e = this.grafo.addEdge(r.getPartenza(), r.getDestinazione());
				if(e == null) {
					Graphs.addEdgeWithVertices(grafo, r.getPartenza(), r.getDestinazione());
				}
				
			}
		}
		System.out.println("vertici: " + this.grafo.vertexSet().size());
		System.out.println("archi: " + this.grafo.edgeSet().size());

		
		
	}
	public Set<Airport> getVertici(){
		return this.grafo.vertexSet();
	}
	
	public Set<DefaultWeightedEdge> getArchi(){
		return this.grafo.edgeSet();
	}
	
	public List<Rotta> getRotte(double distanza){
		return dao.getRotte(distanza, idMap);
	}
	

}
