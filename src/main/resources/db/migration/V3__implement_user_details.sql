ALTER TABLE eve_assist_user
    ADD account_non_expired BOOLEAN;

ALTER TABLE eve_assist_user
    ADD account_non_locked BOOLEAN;

ALTER TABLE eve_assist_user
    ADD credentials_non_expired BOOLEAN;

ALTER TABLE eve_assist_user
    ADD enabled BOOLEAN;