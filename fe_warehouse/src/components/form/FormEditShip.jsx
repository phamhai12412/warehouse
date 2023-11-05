import { Box, Button, Modal, TextField } from '@mui/material'

import React from 'react'

function FormEditShip({ open, handleClose, edit, handleUpdateShip, errorShipName, errorPrice }) {

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
        <form action="" onSubmit={handleUpdateShip} encType="multipart/form-data">
          <TextField id="filled-basic" label="ID" defaultValue={edit.id} InputProps={{ readOnly: true }} name='id' variant="filled" sx={{ margin: '5px 0', display: 'none' }} fullWidth required />
          <TextField error={errorShipName} id="filled-basic" label={errorShipName ? errorShipName : "Name"} defaultValue={edit.shipName} name='shipName' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <TextField error={errorPrice} id="filled-basic" label={errorPrice ? errorPrice : "Price"} defaultValue={edit.price} name='price' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <Button type='submit' sx={{ mt: 2, width: "100%" }} variant="contained"> CẬP NHẬT </Button>
        </form>
      </Box>
    </Modal>
  )
}

export default FormEditShip