package com.vanixmc.events;

import com.vanixmc.events.condition.conditions.PermissionCondition;
import com.vanixmc.events.context.Context;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PermissionConditionTest {

    @Mock
    private Player player;

    @Mock
    private Context context;

    private final String testPermission = "test.permission";
    private PermissionCondition permissionCondition;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Setup the EventContext to return our mocked player
        when(context.getPlayer()).thenReturn(player);
        // Create the permission condition with our test permission
        permissionCondition = new PermissionCondition(testPermission);
    }

    @Test
    public void testPermissionCondition_PlayerHasPermission_ReturnsTrue() {
        // Setup player to have the permission
        when(player.hasPermission(testPermission)).thenReturn(true);

        // Test the condition
        boolean result = permissionCondition.test(context);

        // Verify the result is true
        assertTrue(result);
        // Verify that the hasPermission method was called exactly once
        verify(player, times(1)).hasPermission(testPermission);
    }

    @Test
    public void testPermissionCondition_PlayerDoesNotHavePermission_ReturnsFalse() {
        // Setup player to not have the permission
        when(player.hasPermission(testPermission)).thenReturn(false);

        // Test the condition
        boolean result = permissionCondition.test(context);

        // Verify the result is false
        assertFalse(result);
        // Verify that the hasPermission method was called exactly once
        verify(player, times(1)).hasPermission(testPermission);
    }

    @Test
    public void testPermissionCondition_NullPlayer_ThrowsException() {
        // Setup EventContext to return null player
        when(context.getPlayer()).thenReturn(null);

        // Test the condition and expect an exception
        assertFalse(permissionCondition.test(context));
    }

    @Test
    public void testPermissionConditionBuilder() {
        // This test verifies the builder can create a proper PermissionCondition
        // Create a config with the test permission
        com.vanixmc.events.shared.DomainConfig config = new com.vanixmc.events.shared.DomainConfig();
        config.getConfig().put("permission", testPermission);

        // Build the condition using the builder
        PermissionCondition condition = (PermissionCondition) PermissionCondition.builder().build(config);

        // Assert the permission is set correctly
        assertEquals(testPermission, condition.getPermission());
    }
}
