package me.sheimi.sgit.activities.delegate.actions;

import android.app.AlertDialog;
import android.security.KeyChain;

import me.sheimi.sgit.R;
import me.sheimi.sgit.activities.RepoDetailActivity;
import me.sheimi.sgit.database.models.Repo;

public class MtlsCertAction extends RepoAction {

    public MtlsCertAction(Repo repo, RepoDetailActivity activity) {
        super(repo, activity);
    }

    @Override
    public void execute() {
        String currentAlias = mRepo.getMtlsKeyAlias();
        String statusMsg = (currentAlias != null && !currentAlias.isEmpty())
                ? mActivity.getString(R.string.mtls_cert_current, currentAlias)
                : mActivity.getString(R.string.mtls_no_cert);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(R.string.mtls_action_label)
                .setMessage(statusMsg)
                .setPositiveButton(R.string.mtls_select_cert, (dialog, which) ->
                        KeyChain.choosePrivateKeyAlias(mActivity,
                                alias -> {
                                    if (alias != null) {
                                        mRepo.saveMtlsKeyAlias(alias);
                                    }
                                },
                                null, null, null, -1, currentAlias))
                .setNeutralButton(R.string.mtls_clear_cert, (dialog, which) ->
                        mRepo.saveMtlsKeyAlias(null))
                .setNegativeButton(R.string.label_cancel, null)
                .show();
    }
}
