ALTER TABLE assessment_comment DROP CONSTRAINT assessment_comment_id_user_pk_id_comment_pk;
ALTER TABLE assessment_comment ADD CONSTRAINT assessment_comment_id_assessment_comment_pk PRIMARY KEY (id_assessment_comment);