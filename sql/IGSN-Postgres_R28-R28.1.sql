﻿-- Column: version30.registrant.tc_accepted

-- ALTER TABLE version30.registrant DROP COLUMN tc_accepted;

--add the boolean flag for acceptance of terms and conditions.

ALTER TABLE version30.registrant
    ADD COLUMN tc_accepted boolean NOT NULL DEFAULT false;
