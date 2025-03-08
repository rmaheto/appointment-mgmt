package com.codemaniac.appointment.audit;

import java.io.Serializable;

public interface Auditable extends Serializable {
    Audit getAudit();
}
