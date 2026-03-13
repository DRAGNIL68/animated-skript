package net.outmoded.animated_skript;

import net.outmoded.animated_skript.skript.effects.*;
import net.outmoded.animated_skript.skript.effects.rotation.EffResetRotation;
import net.outmoded.animated_skript.skript.effects.rotation.EffSetRotation;
import net.outmoded.animated_skript.skript.effects.scale.EffResetScale;
import net.outmoded.animated_skript.skript.effects.scale.EffSetScale;
import net.outmoded.animated_skript.skript.effects.tint.EffResetTint;
import net.outmoded.animated_skript.skript.effects.tint.EffSetTint;
import net.outmoded.animated_skript.skript.effects.variant.EffResetActiveVariant;
import net.outmoded.animated_skript.skript.effects.variant.EffSetActiveVariant;
import net.outmoded.animated_skript.skript.events.*;
import net.outmoded.animated_skript.skript.expressions.*;
import net.outmoded.animated_skript.skript.expressions.animation.*;
import net.outmoded.animated_skript.skript.expressions.event.frameset.ExprEventGetEventCurrentFrame;
import net.outmoded.animated_skript.skript.expressions.event.frameset.ExprEventGetEventNewFrame;
import net.outmoded.animated_skript.skript.expressions.event.hitbox.*;
import net.outmoded.animated_skript.skript.expressions.node.*;
import net.outmoded.animated_skript.skript.expressions.variant.*;
import net.outmoded.animated_skript.skript.types.ActiveModel;
import net.outmoded.animated_skript.skript.types.ActiveModelAnimation;
import net.outmoded.animated_skript.skript.types.ActiveModelNode;
import net.outmoded.animated_skript.skript.types.ActiveModelVariant;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class SyntaxRegistration {
    static SkriptAddon addon;

    public static SkriptAddon getAddonInstance() {
        return addon;
    }

    public static void register(){
        addon = ch.njol.skript.Skript.instance().registerAddon(AnimatedSkript.class, "animated-skript");
        SyntaxRegistry syntaxRegistry = addon.registry(SyntaxRegistry.class);

        ActiveModel.register();
        ActiveModelAnimation.register();
        ActiveModelNode.register();
        ActiveModelVariant.register();

        ExprGetActiveModelsActiveAnimations.register(syntaxRegistry);
        ExprGetActiveModelsAnimations.register(syntaxRegistry);
        ExprGetAnimationDuration.register(syntaxRegistry);
        ExprGetAnimationLoopDelay.register(syntaxRegistry);
        ExprGetAnimationLoopMode.register(syntaxRegistry);
        ExprGetAnimationMaxFrameTime.register(syntaxRegistry);
        ExprGetAnimationUuid.register(syntaxRegistry);
        ExprGetCurrentAnimationsCurrentFrameTime.register(syntaxRegistry);
        ExprGetCurrentAnimationsIsPaused.register(syntaxRegistry);
        ExprGetAnimationFromActiveModel.register(syntaxRegistry);

        ExprEventGetEventCurrentFrame.register(syntaxRegistry);
        ExprEventGetEventNewFrame.register(syntaxRegistry);
        ExprEventGetEventCritical.register(syntaxRegistry);
        ExprEventGetEventDamage.register(syntaxRegistry);
        ExprEventGetEventDamageCause.register(syntaxRegistry);
        ExprEventGetEventDamager.register(syntaxRegistry);
        ExprEventGetEventFinalDamage.register(syntaxRegistry);
        ExprEventGetEventUuid.register(syntaxRegistry);

        ExprGetActiveModelsDisplayNode.register(syntaxRegistry);
        ExprGetActiveModelsDisplayNodes.register(syntaxRegistry);
        ExprGetActiveModelsNode.register(syntaxRegistry);
        ExprGetActiveModelsNodes.register(syntaxRegistry);
        ExprGetNodeName.register(syntaxRegistry);
        ExprGetNodeTransformation.register(syntaxRegistry);
        ExprGetNodeType.register(syntaxRegistry);
        ExprGetNodeUuid.register(syntaxRegistry);

        ExprGetActiveModelsActiveVariant.register(syntaxRegistry);
        ExprGetActiveModelsVariants.register(syntaxRegistry);
        ExprGetVariantsDisplayName.register(syntaxRegistry);
        ExprGetVariantsName.register(syntaxRegistry);
        ExprGetVariantsUuid.register(syntaxRegistry);

        ExprGetActiveModel.register(syntaxRegistry);
        ExprGetActiveModel.register(syntaxRegistry);
        ExprGetActiveModelsLocation.register(syntaxRegistry);
        ExprGetActiveModelsScale.register(syntaxRegistry);
        ExprGetActiveModelsRotation.register(syntaxRegistry);
        ExprGetActiveModelsType.register(syntaxRegistry);
        ExprGetAllActiveModels.register(syntaxRegistry);
        ExprGetAllLoadedModels.register(syntaxRegistry);
        ExprGetAnimationFrame.register(syntaxRegistry);
        ExprGetOrigin.register(syntaxRegistry);
        ExprIsPersistent.register(syntaxRegistry);
        ExprLastSpawnedActiveModel.register(syntaxRegistry);

        EffResetRotation.register(syntaxRegistry);
        EffSetRotation.register(syntaxRegistry);

        EffResetScale.register(syntaxRegistry);
        EffSetScale.register(syntaxRegistry);

        EffResetTint.register(syntaxRegistry);
        EffSetTint.register(syntaxRegistry);

        EffAnimationControl.register(syntaxRegistry);
        EffDefaultVisibility.register(syntaxRegistry);
        EffPauseAnimation.register(syntaxRegistry);
        EffReloadModels.register(syntaxRegistry);
        EffRemoveActiveModel.register(syntaxRegistry);
        EffSetPersistence.register(syntaxRegistry);
        EffSetTeleportDuration.register(syntaxRegistry);
        EffSetVisibility.register(syntaxRegistry);
        EffSpawnLoadedModel.register(syntaxRegistry);
        EffStopAllAnimations.register(syntaxRegistry);
        EffStopAnimation.register(syntaxRegistry);
        EffTeleportActiveModel.register(syntaxRegistry);

        EvtOnAnimationEnded.register(syntaxRegistry);
        EvtOnAnimationFrameSet.register(syntaxRegistry);
        EvtOnAnimationPaused.register(syntaxRegistry);
        EvtOnAnimationResumed.register(syntaxRegistry);
        EvtOnAnimationStarted.register(syntaxRegistry);
        EvtOnHitboxAttacked.register(syntaxRegistry);
        EvtOnHitboxInteract.register(syntaxRegistry);
        EvtOnModelRemoved.register(syntaxRegistry);
        EvtOnModelSpawned.register(syntaxRegistry);
        EvtOnReload.register(syntaxRegistry);

        EffResetActiveVariant.register(syntaxRegistry);
        EffSetActiveVariant.register(syntaxRegistry);



    }
}
