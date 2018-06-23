package com.minecolonies.integration;

import com.chaosbuffalo.targeting_api.Faction;
import com.chaosbuffalo.targeting_api.Targeting;
import com.minecolonies.api.colony.permissions.Action;
import com.minecolonies.coremod.colony.jobs.AbstractJobGuard;
import com.minecolonies.coremod.entity.EntityCitizen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.BiFunction;

/**
 * Created by Jacob on 4/18/2018.
 */
public class Integrations {

    private static void setupTargeting(){
        BiFunction<Entity, Entity, Boolean> friendlyWrapper = (caster, target) -> {
            if (caster instanceof EntityCitizen && target instanceof EntityPlayer){
                EntityCitizen worker = (EntityCitizen) caster;
                // Guards are not friends with players they have permission to attack
                if (worker.getCitizenJobHandler().getColonyJob() != null && worker.getCitizenJobHandler().getColonyJob() instanceof AbstractJobGuard){
                    if (!(worker.getCitizenColonyHandler().getColony() != null && worker.getCitizenColonyHandler().getColony().getPermissions().hasPermission(
                            (EntityPlayer) target, Action.GUARDS_ATTACK))){
                        return true;
                    }
                } else {
                    // all other citizens are friends with players
                    return true;
                }
            }
            // We need the reverse targeting of above too.
            if (caster instanceof EntityPlayer && target instanceof EntityCitizen){
                EntityCitizen worker = (EntityCitizen) target;
                if (worker.getCitizenJobHandler().getColonyJob() != null && worker.getCitizenJobHandler().getColonyJob() instanceof AbstractJobGuard){
                    if (!(worker.getCitizenColonyHandler().getColony() != null && worker.getCitizenColonyHandler().getColony().getPermissions().hasPermission(
                            (EntityPlayer) caster, Action.GUARDS_ATTACK))){
                        return true;
                    }
                } else {
                    // all other citizens are friends with players
                    return true;
                }
            }
            if (caster instanceof EntityCitizen && target instanceof EntityCitizen){
                EntityCitizen worker = (EntityCitizen) caster;
                EntityCitizen worker_target = (EntityCitizen) caster;
                if (worker.getCitizenJobHandler().getColonyJob() != null && worker.getCitizenJobHandler().getColonyJob() instanceof AbstractJobGuard){
                    if (worker.getCitizenColonyHandler().getColony().equals(worker_target.getCitizenColonyHandler().getColony())){
                        return true;
                    }
                } else {
                    return true;
                }

            }
            return false;
        };
        Faction farmAnimals = Targeting.getFaction("FarmAnimals");
        farmAnimals.addFriendClass(EntityCitizen.class);
        Targeting.registerFriendlyCallback(friendlyWrapper);
    }

    public static void setup(){
        setupTargeting();
    }
}
