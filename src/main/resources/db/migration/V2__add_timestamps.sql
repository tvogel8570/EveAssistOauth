ALTER TABLE eve_assist_user
    ADD create_timestamp TIMESTAMP WITH TIME ZONE;

ALTER TABLE eve_assist_user
    ADD update_timestamp TIMESTAMP WITH TIME ZONE;

ALTER TABLE eve_assist_user
    ALTER COLUMN create_timestamp SET NOT NULL;

ALTER TABLE eve_assist_user
    ALTER COLUMN update_timestamp SET NOT NULL;