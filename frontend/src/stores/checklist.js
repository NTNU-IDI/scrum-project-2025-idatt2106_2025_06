import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getAllCheckpoints, getAllMyCheckpoints, updateCheckpoints } from '@/service/checklistService.js'

export const useChecklistStore = defineStore('checklist', () => {
  const allCheckpoints = ref([]);
  const selectedCheckpoints = ref([]);
  const percentageCompleted = ref([]);

  async function getChecklist() {
    const response = await getAllCheckpoints();
    allCheckpoints.value = response;
  }

  async function getMySelectedChecklist() {
    const response = await getAllMyCheckpoints();
    selectedCheckpoints.value = response.checkpoints.map(checkpoint => checkpoint.id);
    percentageCompleted.value = response.completionPercentage;
  }

  async function updateChecklist(selectedCheckpoints) {
    await updateCheckpoints(selectedCheckpoints);
    await getMySelectedChecklist();
  }
  return{ allCheckpoints, selectedCheckpoints, percentageCompleted, getChecklist, getMySelectedChecklist, updateChecklist };
})
