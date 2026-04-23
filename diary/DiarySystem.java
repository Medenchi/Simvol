package com.simvol.mod.diary;

import com.simvol.mod.Simvol;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * СИСТЕМА ДНЕВНИКА УЛИК
 * ======================
 * Хранит все найденные улики.
 * Открывается кнопкой или предметом Дневника.
 *
 * Улики добавляются автоматически из DialogueEngine
 * когда игрок находит нужный объект.
 */
public class DiarySystem {

    /** Список найденных улик в порядке добавления */
    private final List<Evidence> foundEvidence = new ArrayList<>();

    /** Улика которую сейчас просматривают в GUI */
    private Evidence selectedEvidence = null;

    // =========================================================
    //  API
    // =========================================================

    /**
     * Добавить улику в дневник.
     * Если улика уже есть — не дублируем.
     */
    public void addEvidence(Evidence evidence) {
        if (evidence == null) return;
        if (foundEvidence.contains(evidence)) return;

        foundEvidence.add(evidence);
        Simvol.LOGGER.info("ДНЕВНИК: Добавлена улика '" + evidence.id() + "'");
    }

    /**
     * Есть ли улика в дневнике.
     */
    public boolean hasEvidence(Evidence evidence) {
        return foundEvidence.contains(evidence);
    }

    /**
     * Все найденные улики (неизменяемый список).
     */
    public List<Evidence> getFoundEvidence() {
        return Collections.unmodifiableList(foundEvidence);
    }

    /**
     * Улики конкретного акта.
     */
    public List<Evidence> getEvidenceForAct(int act) {
        return foundEvidence.stream()
            .filter(e -> e.act() == act)
            .toList();
    }

    public void setSelected(Evidence e) { selectedEvidence = e; }
    public Evidence getSelected()       { return selectedEvidence; }

    public int getTotalFound()    { return foundEvidence.size(); }
    public int getTotalPossible() { return EvidenceRegistry.ALL.size(); }
}
