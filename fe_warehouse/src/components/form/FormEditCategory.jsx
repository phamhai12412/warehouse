import { Box, Button, Modal, TextField } from '@mui/material';

import React from 'react'

function FormEditCategory({ open, handleClose, edit, handleUpdateCategory, errorCategoryName }) {

  const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 500,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
  };

  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        <form action="" onSubmit={handleUpdateCategory} encType="multipart/form-data">
          <TextField id="filled-basic" label="ID" defaultValue={edit.id} InputProps={{ readOnly: true }} name='id' variant="filled" sx={{ margin: '5px 0', display: 'none' }} fullWidth required />
          <TextField error={errorCategoryName} id="filled-basic" label={errorCategoryName ? errorCategoryName : "Name"} defaultValue={edit.categoryName} name='categoryName' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <Button type='submit' sx={{ mt: 2, width: "100%" }} variant="contained"> cập nhật </Button>
        </form>
      </Box>
    </Modal>
  )
}

export default FormEditCategory