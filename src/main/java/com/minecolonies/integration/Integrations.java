package com.minecolonies.integration;

import com.chaosbuffalo.targeting_api.Targeting;
import com.minecolonies.api.colony.permissions.Action;
import com.minecolonies.coremod.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.coremod.colony.jobs.JobGuard;
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
                if (worker.getColonyJob() != null && worker.getColonyJob() instanceof JobGuard){
                    if (!(worker.getColony() != null && worker.getColony().getPermissions().hasPermission(
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
                if (worker.getColonyJob() != null && worker.getColonyJob() instanceof JobGuard){
                    if (!(worker.getColony() != null && worker.getColony().getPermissions().hasPermission(
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
                if (worker.getColonyJob() != null && worker.getColonyJob() instanceof JobGuard){
                    if (worker.getColony().equals(worker_target.getColony())){
                        return true;
                    }
                } else {
                    return true;
                }

            }
            return false;
        };
        Targeting.registerFriendlyCallback(friendlyWrapper);
    }

    public static void setup(){
        setupTargeting();
    }
}
