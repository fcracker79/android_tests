package com.example.mirko.tutorial1.states;

import android.util.Log;

import com.example.mirko.tutorial1.backend.SandboxRepository;

public class SandboxInitState implements SandboxState {

    private static final SandboxState INSTANCE = new SandboxInitState();
    public static SandboxState instance() {
        return INSTANCE;
    }
    private SandboxInitState() {}
    @Override
    public void onEnter(SandboxStateOwner owner, SandboxState previous) {
        owner.setEnableCreateButton(false);
        owner.setEnableSandboxValue(false);
        owner.setEnableSandboxName(true);
        owner.endProgress();
        owner.setSandboxData(null, null);

        /*
        This may happen in case of restoring the channel from the shared preferences
         */
        if (owner.getToken() != null && owner.getToken().length() > 0) {
            this.createClicked(owner);
        }
    }

    @Override
    public void onExit(SandboxStateOwner owner, SandboxState next) {
        // Do nothing
    }

    @Override
    public void createClicked(final SandboxStateOwner owner) {
        owner.startProgress();
        owner.getSandboxRepository().create(
                owner.getToken(),
                new SandboxRepository.SandboxCreationEventListener() {
                    @Override
                    public void onSandboxCreationFailed(int statusCode, Throwable cause) {
                        // FIXME Error handling
                        Log.e("NETWORK", cause.getMessage(), cause);
                        owner.endProgress();
                    }

                    @Override
                    public void onSandboxCreated(String uri, String uid) {
                        owner.setSandboxData(uri, uid);
                        owner.setState(SandboxInSyncState.instance());
                        owner.endProgress();
                        owner.saveChannelInPreferences();
                    }
                }
        );
    }

    @Override
    public void sandboxValueChanged(SandboxStateOwner owner) {

    }

    @Override
    public void sandboxNameChanged(SandboxStateOwner owner) {
        owner.setEnableCreateButton(
                owner.getSandboxName() != null &&
                owner.getSandboxName().toString().trim().length() > 0
        );
    }
}
