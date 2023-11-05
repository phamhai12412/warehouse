

export const validateProductInBill = (bill, productId) => {
  console.log(bill)
  if (bill) {
    for (let i = 0; i < bill.length; i++) {
      for (let j = 0; j < bill[i].billDetais.length; j++) {
        if (bill[i].billDetais[j].product.id === productId) {
          return true;
        }
      }
    }
  }
  return false;
}

export const validateProductInInventory = (inventory, productId) => {
  if (inventory) {
    for (let i = 0; i < inventory.length; i++) {
      for (let j = 0; j < inventory[i].inventoryDetails.length; j++) {
        if (inventory[i].inventoryDetails[j].productId === productId) {
          return true;
        }
      }
    }
  }
  return false;
}

export const validateExistsNameProductUpdate = (products, newName, oldName, categoryId, created, dueDate) => {
  if (products) {
    console.log(products)
    for (let i = 0; i < products.length; i++) {
      if (
        products[i].productName.toUpperCase().replaceAll(" ", "") === newName.toUpperCase().replaceAll(" ", "") &&
        products[i].category.id === categoryId &&
        formatDate(products[i].createdDate) === formatDate(created) &&
        formatDate(products[i].dueDate) === formatDate(dueDate) &&
        newName.toUpperCase().replaceAll(" ", "") !== oldName.toUpperCase().replaceAll(" ", '')
      ) {
        return true;
      }
    }
  }
  return false;
}

const formatDate = (date) => {
  console.log(date)
  const dateArr = date.split("-");
  return `${dateArr[2]}/${dateArr[1]}/${dateArr[0]}`;
}

export const validateInvetoryAndProduct = (products) => {
  let arr = products.filter((item) => (item.statusName !== "PENDING" && item.quantity !== 0));
  if (arr.length > 0) {
    return false;
  }
  return true;
}