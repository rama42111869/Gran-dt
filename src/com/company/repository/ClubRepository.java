package com.company.repository;

import com.company.interfaz.Repository;
import com.company.model.Club;
import com.company.model.Player;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClubRepository implements Repository<Player> {
private static  List<Club> clubs = new ArrayList<>();
private ObjectMapper mapper =new ObjectMapper();
private File fileClub=new File("clubs.json");
@Override
    public void add(Player player) {

    }

    @Override
    public void retrieveData() {
       if(fileClub.exists()) {
           try {
               mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
               mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
               mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
               this.clubs = mapper.readValue(fileClub, new TypeReference<ArrayList<Club>>() {
                   @Override
                   public Type getType() {
                       return super.getType();
                   }
               }).subList(0,19);

           } catch (IOException e) {
               e.printStackTrace();
           }
       }

    }

    public void setClubs(List<Club> clubs) {
        this.clubs = clubs;
    }

    public List<Club> getClubs() {
    retrieveData();
        return this.clubs;
    }

    @Override
    public List<Player> getAll() {
        retrieveData();
        List<Player> players=new ArrayList<>();
        for (Club c:
             this.clubs) {
            for (Player p :
                    c.getPlayerList()) {
                players.add(p);
            }
        }
        return players;
    }

    @Override
    public boolean contains(Player player) {
  return this.clubs.contains(player);
    }


    @Override
    public boolean remove(Player player) {
        if (contains(player)) {
            this.getClubs().remove(player);
            return true;
        }
            return false;

    }

    @Override
    public boolean save() {
       if(fileClub.canWrite()&&fileClub.exists())
       {
           mapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
           try {
               this.mapper.writerWithDefaultPrettyPrinter().writeValue(fileClub,this.clubs);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
        return false;
    }


}
