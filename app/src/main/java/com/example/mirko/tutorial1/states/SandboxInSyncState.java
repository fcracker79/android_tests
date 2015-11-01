package com.example.mirko.tutorial1.states;

import android.util.Log;

import com.example.mirko.tutorial1.SandboxRepository;

/**
 * Created by mirko on 01/11/15.
 */
public class SandboxInSyncState implements SandboxState {
    private static final SandboxState INSTANCE =
            new SandboxInSyncState();

    public static SandboxState instance() {
        return INSTANCE;
    }

    private SandboxInSyncState() {}


    @Override
    public void onEnter(SandboxStateOwner owner, SandboxState previous) {
        owner.setEnableCreateButton(false);
        owner.setEnableSandboxValue(true);
    }

    @Override
    public void onExit(SandboxStateOwner owner, SandboxState next) {

    }

    @Override
    public void createClicked(SandboxStateOwner owner) {
        throw new IllegalStateException();
    }

    @Override
    public void sandboxValueChanged(SandboxStateOwner owner) {
        throw new IllegalStateException();
    }

    @Override
    public void sandboxNameChanged(SandboxStateOwner owner) {
        owner.getSandboxRepository().postData(
                owner.getUid(),
                owner.getSandboxValue(),
                new SandboxRepository.SandboxDataSaveEventListener() {
                    @Override
                    public void onDataSaveFailed(int statusCode, Throwable cause) {
                        // FIXME error handling
                        Log.e("NETWORK", cause.getMessage(), cause);
                    }

                    @Override
                    public void onDataSaved() {
                        // Nothing to do for now
                    }
                }
        );
    }
}
