import { defineStore } from 'pinia'
import { addNewItem, deleteSelected, edit, getAllFromType, getAllItems } from '@/service/inventoryService.js'
import { ref } from 'vue'

export const useInventoryStore = defineStore('inventory', () => {
  const inventory = ref([]);
  const allItems = ref([]);
  const expiresSoonItems = ref([]);
  const registeredItems = ref([]);

  async function addNew(name, type_id, unit_id, storage_id, amount, expiry_date, currentType) {
    const response = await addNewItem(name, type_id, unit_id,  storage_id, amount, expiry_date)
    if (currentType === response.typeId) {
      await getAllFromInventoryType(storage_id, response.typeId)
    }
  }

  async function getAllItemsFromInventory(storageId) {
    let items = [];
    for (let i = 1; i <= 5; i++) {
      const response = await getAllFromType(storageId, i);

      if (!Array.isArray(response)) {
        console.error('Forventet en inventory liste, men fikk:', response);
        inventory.value = [];
        return;
      }

      items.push(...response);
    }
    allItems.value = items;
  }

  async function getExpiresSoonItems(storageId) {
    const todayDate = new Date(Date.now());
    console.log(todayDate);
    todayDate.setHours(0, 0, 0, 0);

    const monthsUntilExpiryToShow = 3;
    const untilExpiryToShowDate = new Date();
    untilExpiryToShowDate.setHours(0, 0, 0, 0);
    untilExpiryToShowDate.setMonth(untilExpiryToShowDate.getMonth() + monthsUntilExpiryToShow);

    console.log(expiresSoonItems);
    await getAllItemsFromInventory(storageId);
    expiresSoonItems.value = allItems.value
      .filter((item) => {
        const expiryDate = new Date(item.expiryDate).getTime();
        return expiryDate > Date.now() && expiryDate < untilExpiryToShowDate.getTime();
      })
      .sort((a, b) => new Date(a.expiryDate) - new Date(b.expiryDate));
    console.log(expiresSoonItems);

  }

  async function getAllFromInventoryType(storageId, typeId) {
    const response = await getAllFromType(storageId, typeId);

    if (!Array.isArray(response)) {
      console.error('Forventet en inventory liste, men fikk:', response);
      inventory.value = [];
      return;
    }

    inventory.value = response;
  }

  async function deleteAllSelectedItems(selectedIds, storage_id, type_id) {
    await deleteSelected(selectedIds);

    await getAllFromInventoryType(storage_id, type_id)
  }

  async function getAllRegisteredItems() {
    const response = await getAllItems();

    if (!Array.isArray(response)) {
      console.error('Forventet en liste, men fikk:', response);
      registeredItems.value = [];
      return;
    }
    registeredItems.value = response;
  }

  async function editItemInstance(itemId, amount, expiryDate, storage_id, type_id) {
    await edit(itemId, amount, expiryDate);
    await getAllFromInventoryType(storage_id, type_id)
  }

  return{ inventory, registeredItems, allItems, expiresSoonItems, addNew, getAllItemsFromInventory, getExpiresSoonItems, getAllFromInventoryType, deleteAllSelectedItems,
    getAllRegisteredItems, editItemInstance };
})
