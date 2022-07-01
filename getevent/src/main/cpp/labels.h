#ifndef LABELS_H
#define LABELS_H

#ifndef LABEL
    #error "please define `LABEL` first!"
#endif

#ifndef LABEL_END
    #error "please define `LABEL_END` first!"
#endif

#include <linux/input-event-codes.h>

static label val_labels[] = {
    { "UP", 0 },
    { "DOWN", 1 },
    { "REPEAT", 2 },
    LABEL_END
};

static label ev_labels[] = {
    LABEL(EV_SYN),
    LABEL(EV_KEY),
    LABEL(EV_ABS),
    LABEL_END
};

static label syn_labels[] = {
    LABEL(SYN_REPORT),
    LABEL_END
};

static label key_labels[] = {
    LABEL(BTN_TOUCH),
    LABEL_END
};


static label abs_labels[] = {
    LABEL(ABS_MT_SLOT),
    LABEL(ABS_MT_POSITION_X),
    LABEL(ABS_MT_POSITION_Y),
    LABEL(ABS_MT_TRACKING_ID),
    LABEL_END,
};

#endif // LABELS_H
